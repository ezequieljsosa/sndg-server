
if (!String.prototype.trim) {
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g,'');
	}
}


/**
 * Agrego los métodos que necesito al prototipo de GLmol 
 */
GLmol.prototype.removeAlphaSpheres = function(atomlist) {
	var ret = [];
	for (var i in atomlist) {
		var atom = this.atoms[atomlist[i]]; if (atom == undefined) continue;

		if (atom.resn != 'STP') ret.push(atom.serial);
	}
	return ret;
};

GLmol.prototype.getAlphaSpheres = function(atomlist) {
	
	var ret = [];
	var alpha_pol = [];
	var alpha_apol = [];
	for (var i in atomlist) {
		var atom = this.atoms[atomlist[i]]; if (atom == undefined) continue;

		if (atom.resn == 'STP'){
			if (atom.atom == 'POL'){
				alpha_pol.push(atom.serial)
			}else if (atom.atom == 'APOL'){
				alpha_apol.push(atom.serial)
			}
		}
	}
	return new Array(alpha_pol, alpha_apol);
};

/**
 * Constructor de PDBParser
 * @returns
 */
function PDBParser() {
	var _correctedPdbString;
	var _csaPdbString;
	var _featuresPdbString;
	var _pdbPfamString;
	var _csaResidues = [];
	var _pdbPfamResidues = [];
	var _residueAtomsMap = [];
	

	this.getResidueAtomsMap = function() {
		return _residueAtomsMap;
	}

	/**
	 * Verifica que la linea del pdb se corresponda con un registro de un atomo.
	 */
	this.isATOMLine = function( pdbLine ) {
		return pdbLine.substring(0, 4) == 'ATOM';
	}

	
	
	this.arrayContainsResidue = function( res, resArray ) {
		res_position = res.posicion; 
		for ( var i = 0; i < resArray.length; i++ ) {
			var elemArray = resArray[i];
			if (elemArray.posicion == res_position && elemArray.cadena == res.cadena) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica si la linea corresponde a un átomo que forma parte de los residuos pasados por parametros.
	 *  La función no modifica la linea pasada de ninguna forma.
	 * @param pdbLine
	 */
	this.isResidueInLine = function( pdbLine, residues ) {		
		var resSeq = parseInt(pdbLine.substring(22, 26).trim()); // 22 a 27
		var resChain = pdbLine.substring(21, 22).trim();

		return this.arrayContainsResidue( {posicion: resSeq, cadena: resChain },residues);
	}


	this.buildPdbString = function(pdb, residues) {
		var pdbString = 'HEADER    Encabezado falso para GLmol             01-SEP-11   FALS\n';
		
		_residueAtomsMap = [];

		var line = '';
		var previousIndex = 0;
		var index = -1;
		
		while ( (index = pdb.indexOf('\n', previousIndex)) != -1 ) {
			
			line = pdb.substring(previousIndex, index+1);
			
			if ( this.isATOMLine(line) ) {
				if ( this.isResidueInLine( line,residues ) ) {
					pdbString += line;
				}
				var atomSeq = parseInt(line.substr(6, 5));
				var resSeq = parseInt(line.substr(22, 5));
				_residueAtomsMap[atomSeq] = resSeq;
			}
			_correctedPdbString += line;
			previousIndex = index +1;
		}
		// Procesar la última linea, si es una linea sin \n
		pdbString += 'TER \nEND \n';

		return pdbString;
	}

	this.parsePdb = function( pdb ) {
		_correctedPdbString = '';
		_csaPdbString = this.buildPdbString(pdb,_csaResidues);
		_correctedPdbString = ''; // Parche para manejar dos llamadas a this.buildPdbString(...) -> el método fue pensado para una llamada, hace append a _correctedPdbString
		_pdbPfamString = this.buildPdbString(pdb,_pdbPfamResidues);	  
	}

	this.addHetatm = function(data) {
		var insertionPosition = -1;
		var lastHetatmIndex = _correctedPdbString.lastIndexOf('HETATM');
		if (lastHetatmIndex < 0) {
			// No tiene HETATM, voy por el último 'TER' :)
			var lastTerIndex = _correctedPdbString.lastIndexOf('TER');
			insertionPosition = _correctedPdbString.indexOf('\n', lastTerIndex) + 1;
		} else {
			insertionPosition = _correctedPdbString.indexOf('\n', lastHetatmIndex) + 1;
		}		
		_correctedPdbString = _correctedPdbString.substring(0, insertionPosition) + data + _correctedPdbString.substring(insertionPosition);
	}

	/**
	 * Toma un PDB y retorna una lista con los números de serie de los átomos
	 * Sirve para tener la lista de átomos de un pocket, con referencia el PDB
	 * original al cual corresponde el pocket.
	 */
	this.getAtomsPDB = function( data ) {
		
		var atoms = [];
		var atomSeq = -1;
		var previousIndex = 0;
		var index = -1;
		while ( (index = data.indexOf('\n', previousIndex)) != -1 ) {
			line = data.substring(previousIndex, index+1);
			if ( this.isATOMLine(line) ) { // Solo si es átomo parseo la linea
				atomSeq = parseInt(line.substr(6, 5));
				atoms.push( atomSeq );
			}
			previousIndex = index +1;
		}
		return atoms;
	}

	this.getAtomsSubPDB = function(extendedPocketAtoms) {
		var newPDBString = '';
		var pdb = this.getCorrectedPdbString();

		var line = '';
		var previousIndex = 0;
		var index = -1;
		while ( (index = pdb.indexOf('\n', previousIndex)) != -1 ) {
			line = pdb.substring(previousIndex, index+1);
			if ( this.isATOMLine(line) ) {
				atomSeq = parseInt(line.substr(6, 5));
				if (extendedPocketAtoms.indexOf(atomSeq) != -1) { // Está -> escribir al resultado
					newPDBString += line;
				}
			}
			previousIndex = index +1;
		}
		return newPDBString;
	}

	/**
	 * Recibe una lista de átomos (serial number en el pdb) y devuelve la lista de
	 * todos los átomos en los residuos a los que pertenecen. Por ejemplo, en:
	 * 
	 * ATOM    745  OG  SER A 125     -28.794 -57.040  39.533  1.00112.26           O  
	 * ATOM    746  N   LEU A 126     -25.457 -55.187  37.616  1.00110.95           N  
	 * ATOM    747  CA  LEU A 126     -25.000 -53.804  37.701  1.00109.72           C  
	 * ATOM    748  C   LEU A 126     -25.260 -53.078  36.380  1.00108.69           C  
	 * ATOM    749  O   LEU A 126     -25.279 -53.700  35.315  1.00108.54           O  
	 * ATOM    750  CB  LEU A 126     -23.506 -53.786  38.025  1.00109.93           C  
	 * ATOM    751  CG  LEU A 126     -22.892 -52.518  38.618  1.00110.65           C  
	 * ATOM    752  CD1 LEU A 126     -23.457 -52.267  40.008  1.00110.15           C  
	 * ATOM    753  CD2 LEU A 126     -21.379 -52.682  38.683  1.00110.93           C  
	 * ATOM    754  N   THR A 127     -25.461 -51.764  36.451  1.00107.35           N  
	 * 
	 * Si se le pasa [750] debería ver que pertenece a la Leucina 126 de la cadena A e incluir
	 * en los resultados a [746, 747, 748, 749, 750, 751, 752, 753].
	 **/
	this.getPartnersAtomsInResidues = function( atoms, residueAtomsMap ) {
		var partnerAtoms = [];
		var residues = [];
		for (var i=0; i<atoms.length; i++) {
			var resi = residueAtomsMap[ atoms[i] ];
			// Chequeo que no esté de antes
			if (residues.indexOf(resi) == -1) {
				residues.push(resi);
				var ini = atoms[i];
				while (residueAtomsMap[ini] == resi) {
					ini--;
				}
				ini++;
				var fin = atoms[i];
				while (residueAtomsMap[fin] == resi) {
					fin++;
				}
				// Agrego todo el intervalo
				for (var partnerAtom=ini; partnerAtom<fin; partnerAtom++) {
					partnerAtoms.push(partnerAtom);
				}
			}
		}
		return partnerAtoms;
	}
	this.getCorrectedPdbString = function() {
		return _correctedPdbString;
	}
	this.getCsaPdbString = function() {
		return _csaPdbString;
	}
	this.getFeaturesPdbString = function() {
		return _featuresPdbString;
	}
	this.getPdbPfamString = function() {
		return _pdbPfamString;
	}
	this.setCsaResidues = function( csaResidues ) {
		_csaResidues = csaResidues;
	}
	this.getCsaResidues = function() {
		return _csaResidues;
	}
	this.setFeaturesResidues = function( featuresResidues ) {
		_featuresResidues = featuresResidues;
	}
	this.getFeaturesResidues = function() {
		return _featuresResidues;
	}
	this.setPdbPfamResidues = function( pdbPfamResidues ) {
		_pdbPfamResidues = pdbPfamResidues;
	}
	this.getPdbPfamResidues = function() {
		return _pdbPfamResidues;
	}
}

