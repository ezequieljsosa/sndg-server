/**
 * Esta clase tiene el manejo de cambios en la UI (se le pasan los eventos) y
 * cualquier cambio que se deba hacer a la UI. Como el modelo no se cambia desde
 * otro lado, no hay dilema para decidir si la actualización MODELO -> UI debe
 * hacerse directamente o a travez de este controller.
 * 
 * @returns
 */
function PDBPanelPresenter(model, view) {
	this._model = model;
	this._view = view;
}

PDBPanelPresenter.prototype.getModel = function() {
	return this._model;
}

PDBPanelPresenter.prototype.destroy = function() {
	this._view.deleteView();
	this._model.deleteModel();
}

PDBPanelPresenter.prototype.init = function() {
	var model = this._model;

	// REGISTER de los pockets (check + color)
	this._view.registerPocketCheckHandler(function(val1, val2) {
		model.setPocketActive(val1, val2);
	});
	this._view.registerPocketColorHandler(function(val1, val2) {
		model.setPocketColor(val1, val2);
	});

	// REGISTER de los checks (boolean)
	this._view.registerCsaHandler(function(val) {
		model._csa = val;
	});

	this._view
			.registerFeatureHandler(function(val) {
				var featuresResidues = [];
				featureData = val.split("#");
				var cadena = "";
				
				if(featureData.length > 3){
					cadena = featureData[3]
				}
				for (var x = parseInt(featureData[1]); x <= parseInt(featureData[2]); x++) {
					position = model.mapSequenceResidue(x,cadena);
					featuresResidues.push({
						posicion : position,
						cadena : cadena
					});
				}
				
				model._feature = pdbParser.buildPdbString($('#glmol_pdb_src').val(), featuresResidues);
				
			});

	this._view.registerHetatmHandler(function(val) {
		model._hetatm = val;
	});
	this._view.registerPfamImportantResiduesHandler(function(val) {
		model._pfamImportantResidues = val;
	});

	// REGISTER de los combos (string)
	this._view.registerProteinVisualizationHandler(function(val) {
		model._proteinVisualization = val;
	});
	this._view.registerPocketsVisualizationHandler(function(val) {
		model._pocketsVisualization = val;
	});

	// REGISTER de los botones (behaviour)
	this._view.registerVMDButtonHandler(function(val) {
		model.downloadVMD();
	});

	// El clear se registra desde la misma vista, es una tarea interna (la rel.
	// con el modelo es a travez de los handlers)

	this._view.registerUpdateButtonHandler(function(val) {
		model.updateGlmol();
	});

	this._view.init();
	this._model.init();

} // end método init

// END panelPresenter

/**
 * 
 */
function PDBPanelView() {
	this._colorChecks = $('#glmolPocketTable').find('.pocket_check'); // Registro
	// para
	// todos
	// los
	// checks
	this._colorSelector = $('#glmolPocketTable').find('.pocket_color'); // Registro
	// para
	// todos
	// los
	// selectores
	// de
	// color
}

// REGISTERS de check y color para los POCKETS
// Diseño: considero un solo handler con parámetro nroPocket
PDBPanelView.prototype.registerPocketCheckHandler = function(pocketCheckHandler) {
	// var elementos = $('#glmolPocketTable').find('.pocket_check'); // Registro
	// para todos los checks
	this._colorChecks.each(function() {
		
//		$('#hetatomCheck').on('ifToggled', function(event) {		
//			hetatmHandler.call($('#hetatomCheck'), this.checked);
//		});
		
		$(this).on('change',function() {			
			var val = this.checked;
			var id = this.id;
			var pocketNumberStr = id.substring(id.lastIndexOf('_') + 1);
			pocketCheckHandler.call(this, pocketNumberStr, val);
		});
	});
}

PDBPanelView.prototype.registerPocketColorHandler = function(pocketColorHandler) {
	// var elementos = $('#glmolPocketTable').find('.pocket_color'); // Registro
	// para todos los checks
	this._colorSelector.each(function() {
		$(this).change(function() {
			var val = $(this).val();
			var id = this.id;
			var pocketNumberStr = id.substring(id.lastIndexOf('_') + 1);
			pocketColorHandler.call(this, pocketNumberStr, val);
		});
	});
}

// REGISTERS de checks-booleans
PDBPanelView.prototype.registerHetatmHandler = function(hetatmHandler) {

	$('#hetatomCheck').on('ifToggled', function(event) {		
		hetatmHandler.call($('#hetatomCheck'), this.checked);
	});

}
PDBPanelView.prototype.registerCsaHandler = function(csaHandler) {
	$('#csaCheck').change(function() {
		var val = this.checked;
		csaHandler.call(this, val);
	});
}

PDBPanelView.prototype.registerFeatureHandler = function(featureHandler) {
	$('#feature').change(function() {
		var val = this.value;
		featureHandler.call(this, val);
	});
}

PDBPanelView.prototype.registerPfamImportantResiduesHandler = function(
		pfamImportantResiduesHandler) {
	$('#pfamCheck').change(function() {
		var val = this.checked;
		pfamImportantResiduesHandler.call(this, val);
	});
}

// REGISTERS de combos-string
PDBPanelView.prototype.registerProteinVisualizationHandler = function(
		protVisualizationHandler) {
	$('#proteinVisualization').change(function() {
		var val = $(this).val();
		protVisualizationHandler.call(this, val);
	});
}
PDBPanelView.prototype.registerPocketsVisualizationHandler = function(
		pocketsVisualizationHandler) {
	$('#pocketsVisualization').change(function() {
		var val = $(this).val();
		pocketsVisualizationHandler.call(this, val);
	});
}

// REGISTERS de botones
PDBPanelView.prototype.registerUpdateButtonHandler = function(
		updateButtonHandler) {
	$('#updateButton').click(function(e) {
		updateButtonHandler.call(this);
	});
}
PDBPanelView.prototype.registerClearButtonHandler = function(clearButtonHandler) {
	$('#clearButton').click(function() {
		clearButtonHandler.call(this);
	});
}
PDBPanelView.prototype.registerVMDButtonHandler = function(vmdButtonHandler) {
	$('#vmdButton').click(function() {
		vmdButtonHandler.call(this);
	});
}

// Registro _clearView como handler del evento en la misma UI (tarea interna)
PDBPanelView.prototype.init = function() {	
	var colorChecks = this._colorChecks;
	var colorSelector = this._colorSelector;

	$('#clearButton').click(
			function() {
				console.info("Limpiando la vista...");
				colorChecks.each(function(index, colorCheckElem) {
					$(colorCheckElem).attr('checked', false);
					$(colorCheckElem).change();
				});
				colorSelector.each(function(index, colorPickerElem) {
					$(colorPickerElem).val(
							'#' + $.fn.colorPicker.defaults.colors[index]);
					$(colorPickerElem).change();
				});

				$('#hetatomCheck').attr('checked', false); // No dispara evento
				// change, lo tiro
				// para que se
				// actualice el
				// modelo
				$('#csaCheck').attr('checked', false);
				$('#csaCheck').change();
				$('#feature').attr('value', '');
				$('#feature').change();
				$('#pfamCheck').attr('checked', false);
				$('#pfamCheck').change();
				$('#proteinVisualization').val('chain');
				$('#proteinVisualization').change();
				$('#pocketsVisualization').val('atoms');
				$('#pocketsVisualization').change();

				// Lanza el evento del otro botón (el handler lo mando directo
				// al elem HTML, así que no tengo ref al mismo simil Action)
				$('#updateButton').click();
			});

	this._colorSelector.each(function(index, elem) {		
		$(elem).val('#' + $.fn.colorPicker.defaults.colors[index]);
		$(elem).change();
	});
}

PDBPanelView.prototype.deleteView = function() {
	$('#glmol_pdb').remove();
	$('#hetatomCheck').remove();
	$('#csaCheck').remove();
	$('#pfamCheck').remove();

	$('#glmolPocketTable').remove();
	$('#pocketsVisualization').remove();
	$('#proteinVisualization').remove();
	$('#updateViewButton').remove();
	$('#clearViewButton').remove();
	$('#vmdButton').remove();
}
// END panelView

/**
 * Esta clase tiene la información y el comportamiento, incluyendo la comm con
 * el server (ajax)
 * 
 * @returns objeto PDBPanelModel para manejar el comportamiento de la UI
 */
function PDBPanelModel(pdbParser) {
	this._pdbId = '';
	this._compositeId = '';
	this._glmol_pdb = '';
	this._glmol_pockets = {};
	this._pdbParser = pdbParser;
	this._pocketsConf = {}; // mapa (associative array) con elems {active: t/f,
	// color: #FFF}

	// DATOS booleanos
	this._hetatm = true;
	this._csa = false;
	this._feature = false;
	this._pfamImportantResidues = false;
	// DATOS string
	this._proteinVisualization = "chain";
	this._pocketsVisualization = "atoms";
	
	/**
	 * Clave: Cadena
	 * Valor: mapa de
	 * 	 Clave numero de secuencia en pdbseqres, es un string
	 * 	 Valor numero de residuo en el pdb
	 * */
	var _residueMap = null;
}

PDBPanelModel.prototype.mapSequenceResidue = function(seqResNum,chain){
	if(this._residueMap != null){
		return this._residueMap[chain][seqResNum.toString()]
	} else {
		return seqResNum;
	}
}

/**
 * pocketNumber debe ser un string para usar _pocketsConf como obj y no como
 * array (tiene fragmentación interna...)
 */
PDBPanelModel.prototype.setPocketActive = function(pocketNumber, active) {
	console.info('POCKET NRO ' + pocketNumber + ' activado = ' + active);
	var pockConf = this._pocketsConf[pocketNumber];
	if (pockConf == undefined) {
		pockConf = {};
	}
	pockConf.active = active;
	this._pocketsConf[pocketNumber] = pockConf;
}

PDBPanelModel.prototype.getPocketActive = function(pocketNumber) {
	var pockConf = this._pocketsConf[pocketNumber];
	if (pockConf == undefined) {
		return false;
	} else {
		if (pockConf.active == undefined) {
			return false;
		} else {
			return pockConf.active;
		}
	}
}

/**
 * pocketNumber debe ser un string para usar _pocketsConf como obj y no como
 * array (tiene fragmentación interna...)
 */
PDBPanelModel.prototype.setPocketColor = function(pocketNumber, color) {
	console.info('POCKET NRO ' + pocketNumber + ' color = ' + color);
	var pockConf = this._pocketsConf[pocketNumber];
	if (pockConf == undefined) {
		pockConf = {};
	}
	pockConf.color = color;
	this._pocketsConf[pocketNumber] = pockConf;
}

PDBPanelModel.prototype.getPocketColor = function(pocketNumber) {
	var pockConf = this._pocketsConf[pocketNumber];
	if (pockConf == undefined) {
		return null;
	} else {
		if (pockConf.color == undefined) {
			return null;
		} else {
			return pockConf.color;
		}
	}
}

/**
 * Pide al server el zip con la visualización para VMD, con la configuración
 * actual de la visualización de glmol
 */
PDBPanelModel.prototype.downloadVMD = function() {
	// El conocimiento del llamado al server y sus parámetros acá!

	var urlBase = '../rest/structure/' + this._pdbId.toUpperCase() + '/download';


	for ( var pocketNumber in this._pocketsConf) {
		var pockConf = this._pocketsConf[pocketNumber];
		if (pockConf != undefined && pockConf.active) {
			urlBase += '&pocketNumbers=' + pocketNumber;
		}
	}
	$('#vmdDownloadLink').attr('href', urlBase);
	window.location = urlBase;
}

/**
 * Actualiza la visualización del panel de glmol con las opciones actuales
 * seleccionadas en la UI
 */
PDBPanelModel.prototype.updateGlmol = function() {

	// Dereferencio this, adentro de la función anónima cambia el this (cambia
	// el contexto)
	var priv_model = this;

	var glmol_csa;
	var glmol_pfam;
	var glmol_features;

	for ( var pocketNumber in this._pocketsConf) {
		var pockConf = this._pocketsConf[pocketNumber];

		if (pockConf != undefined && pockConf.active) {
			if ($('#glmol_pocket_' + pocketNumber + '_src').length == 0) {
				this.cargarPocketPDB(pocketNumber);
			}
			try {				
				this._glmol_pockets[pocketNumber] = new GLmol('glmol_pocket_'
						+ pocketNumber, true);
				this._glmol_pockets[pocketNumber].loadMolecule();
			} catch (err) {
				console.info('Error creando GLMOL del pocket ' + pocketNumber
						+ ' -> ' + err.message);
			}
		}
	}

	if (this._csa) {
		glmol_csa = new GLmol('glmol_csa', true);
		glmol_csa.loadMolecule();
	} else {
		glmol_csa = undefined;
	}

	if (this._feature) {		
		$('#glmol_features_src').val(this._feature);
		glmol_features = new GLmol('glmol_features', true);
		glmol_features.loadMolecule();

	} else {
		glmol_features = undefined;
	}

	if (this._pfamImportantResidues) {
		glmol_pfam = new GLmol('glmol_pfam', true);
		glmol_pfam.loadMolecule();
	} else {
		glmol_pfam = undefined;
	}

	console.info('REDIBUJANDO');
	// var priv_glmol_pockets = this._glmol_pockets;

	this._glmol_pdb.defineRepresentation = function() {
		// Tomo datos del glmol
		
		var all = this.getAllAtoms();
		var allHetatm = this.getHetatms(all);
		// Quito los Alpha Spheres (método agregado a glmol)
		all = this.removeAlphaSpheres(all);
		// Tomo datos del glmol
		

		var hetatm = this.removeSolvents(allHetatm);
		var noAlphaHetatm = this.removeAlphaSpheres(hetatm);
		var alphaHetatm = this.getAlphaSpheres(hetatm);
		var alphaHetatm_pol = alphaHetatm[0];
		var alphaHetatm_apol = alphaHetatm[1];

		console.info('# HETATM ALL -> ' + allHetatm.length);
		console.info('# HETATM NON-WATER -> ' + hetatm.length);

		var asu = new THREE.Object3D();

		if (priv_model._proteinVisualization == 'chain') {
			this.colorByChain(all);
			this.drawCartoon(asu, all, this.curveWidth, this.thickness);
		} else if (priv_model._proteinVisualization == 'spectrum') {
			this.colorChainbow(all);
			this.drawCartoon(asu, all, this.curveWidth, this.thickness);
		} else if (priv_model._proteinVisualization == 'b-factor') {
			this.colorByBFactor(all);
			this.drawCartoon(asu, all, this.curveWidth, this.thickness);
		} else if (priv_model._proteinVisualization == 'bonds') {
			this.colorByAtom(all, {});
			this.drawBondsAsLine(asu, all, 2.0);
		} else {
			this.colorChainbow(all);
			this.drawCartoon(asu, all, this.curveWidth, this.thickness);
		}
		
		if (priv_model._hetatm) {
			this.colorByAtom(noAlphaHetatm, {});
			// this.drawBondsAsStick(asu, noAlphaHetatm, this.cylinderRadius
			// /1.5, this.cylinderRadius, true, false, 0.4);
			this.drawAtomsAsSphere(asu, noAlphaHetatm, 1.5,
					1.5 * this.thickness);
		}
		if (priv_model._csa) {
			this.colorByAtom(glmol_csa.getAllAtoms(), {});
			this.drawBondsAsStick(asu, glmol_csa.getAllAtoms(), 0.2, 0.4, true,
					false, 0.3);
		}

		if (priv_model._feature) {
			this.colorByAtom(glmol_features.getAllAtoms(), {});
			this.drawBondsAsStick(asu, glmol_features.getAllAtoms(), 0.2, 0.4,
					true, false, 0.3);
		}

		if (priv_model._pfamImportantResidues) {
			this.colorByAtom(glmol_pfam.getAllAtoms(), {});
			this.drawBondsAsStick(asu, glmol_pfam.getAllAtoms(), 0.2, 0.4,
					true, false, 0.3);
		}

		// Revisar uso de features
		// if (pocketsConf.features) {
		// this.colorByAtom(glmol_features.getAllAtoms(), {});
		// this.drawBondsAsStick(asu, glmol_features.getAllAtoms(), 0.2, 0.4,
		// true, false, 0.3);
		// }

		var j = 0;

		/*
		 * TODO Iterar por las keys de _pocketsConf y acceder a los elem que
		 * tiene (values)
		 */
		for ( var pocketNumber in priv_model._pocketsConf) {
			var pockConf = priv_model._pocketsConf[pocketNumber];
			if (pockConf != undefined && pockConf.active) {
				if (priv_model._pocketsVisualization == 'alphaSpheres') {
					
					// pocketsConf.pocketsVisualization
					// filtrar los alphaHetatm por el pocket 'i'

					var misAlpha_pol = this.getResiduesById(alphaHetatm_pol,
							'   ' + (1 + parseInt(pocketNumber)));
					var misAlpha_apol = this.getResiduesById(alphaHetatm_apol,
							'   ' + (1 + parseInt(pocketNumber)));

					this.colorAtoms(misAlpha_apol, parseInt('0xD9D9D9'));
					this.colorAtoms(misAlpha_pol, parseInt('0x'
							+ pockConf.color.substring(1)));

					var misAlphaTotal = misAlpha_pol.concat(misAlpha_apol);
					;
					this.drawAtomsAsSphere(asu, misAlphaTotal, 2.0,
							2 * this.thickness);

				} else if (priv_model._pocketsVisualization == 'atoms') {
					if (pockConf.color != '#bbbbbb') {
						this.colorAtoms(priv_model._glmol_pockets[pocketNumber]
								.getAllAtoms(), parseInt('0x'
								+ pockConf.color.substring(1)));
						this.drawAtomsAsSphere(asu,
								priv_model._glmol_pockets[pocketNumber]
										.getAllAtoms(), 1.0, this.thickness);
					} else {
						this.colorByAtom(
								priv_model._glmol_pockets[pocketNumber]
										.getAllAtoms(), {});
						this.drawBondsAsStick(asu,
								priv_model._glmol_pockets[pocketNumber]
										.getAllAtoms(), 0.2, 0.4, true, false,
								0.3);
					}
				} else {
					console.error('No se conoce la visualización de pockets '
							+ priv_model._pocketsVisualization);
				}
			}
		}
		this.drawCartoonNucleicAcid(asu, all);
		this.drawNucleicAcidStick(this.modelGroup, all);
		this.modelGroup.add(asu);
	};
	this._glmol_pdb.rebuildScene();
	this._glmol_pdb.show();

}

PDBPanelModel.prototype.init = function() {
	// Traigo el PDB para glmol
	this.cargarPDB();
	
	
}

PDBPanelModel.prototype.cargarPDB = function() {
	// var url = $('#pdbLink').attr('href');
	var url = "../rest/structure/";
	var pdbId = this._pdbId;
	var pdbParser = this._pdbParser;
	var model = this;

	$.ajax({
		type : "GET",
		url : url + pdbId +   '/pdb',
		// dataType : "text/plain",
		async : false,
		success : function(data) {
			// pdbParser.setCsaResidues(csaResidues);
			// pdbParser.setPdbPfamResidues(pdbPfamResidues);

			pdbParser.parsePdb(data);
			// $("#glmol_csa_src").val(pdbParser.getCsaPdbString());
			// $("#glmol_pfam_src").val(pdbParser.getPdbPfamString());

			 $("#glmol_pdb_src").val(pdbParser.getCorrectedPdbString());
			//$("#glmol_pdb_src").val(data);
			
			 model.cargarFpocketOut();
			
			
		},
		fail : function(e) {
			alert(e);
		}
	});
}

PDBPanelModel.prototype.cargarFpocketOut = function() {
	// var url = $('#fpocketOutLink').attr('href');
	var pdbId = this._pdbId
	var url = '../rest/structure/' + pdbId + "/heatatom";
	var pdbParser = this._pdbParser;
	var model = this;
	$.get(url)
	// type : "GET",
	// url : url,
	// dataType : "json",
	// async : false,
	// data : {
	// "pdbId" : function() {
	// return pdbId;
	// }
	// },
	.fail(function(request, error) {
		alert(error)
	}).success(function(data) {	
		
		if(data.trim().length < 10){
			$('#hetatom_section').remove();
			
		} else {
			pdbParser.addHetatm(data);
			$("#glmol_pdb_src").val(pdbParser.getCorrectedPdbString());
		}
		
		
		
		
		
	}).always(function(){
		
		model.cargarFeatures();
		model._glmol_pdb = new GLmol('glmol_pdb');

		model.updateGlmol();

		if (model.on_end != undefined){
			model.on_end();
		} 
	});

}

PDBPanelModel.prototype.cargarPocketPDB = function(_pocketNumber) {
	// var url = $('#pocketLink').attr('href');
	
	var pdbId = this._pdbId;
	var pdbParser = this._pdbParser;
	var pocketNumber = _pocketNumber;
	var url = '../rest/structure/' + pdbId  + '/pocket/' + _pocketNumber;
	
	$.ajax({
		type : "GET",
		url : url,
		async : false,
		//dataType : "json",
//		data : {
//			// Gestionar variable con el pdbId
//			'pdbId' : function() {
//				return pdbId
//			},
//			'pocketNr' : function() {
//				return pocketNumber
//			}
//		},
		
		success : function(data) {
			/**
			 * Hago la transformación listaAtoms -> listaResidues ->
			 * listaExtendidaAtoms La listaExtendidaAtoms tiene todos los atoms
			 * en los residuos en que hay un atom de la lista original Para eso
			 * parseo el pdb, iterar por las lineas (¿no se hace eso ya en algún
			 * lado?)
			 * 
			 * Con la lista extendida hay que componer el texto nuevo para el
			 * fpocket
			 */			
			
			var newData = data.substring(0, data.indexOf('ATOM'));
			var pocketAtoms = pdbParser.getAtomsPDB(data);
			var extendedPocketAtoms = pdbParser.getPartnersAtomsInResidues(
					pocketAtoms, pdbParser.getResidueAtomsMap());
			var extra = pdbParser.getAtomsSubPDB(extendedPocketAtoms);
			newData = newData + extra;
			newData = newData
					+ data.substring(data.lastIndexOf('TER'), data.length);
			// Agrego pocket
			$('#glmolForm').append(
					'<input type="hidden" id="glmol_pocket_' + pocketNumber
							+ '_src" class="pocket_data_src" name="noNamePock'
							+ pocketNumber + '" value="' + newData + '">');
		}
	});
}

PDBPanelModel.prototype.cargarFeatures = function() {
	var compositeId = this._compositeId;
	var model = this;
	$.ajax({
		// url : '../../hmm/ajax/getHmm.do?idEntry=' + compositeId,
		url : '../rest/structure/' + compositeId + "/features",
		type : 'GET',
		data : '',
		dataType : 'json',		
		success : function(json) {
			var options = '<option value="">Select</option>';
			
			$.each(json, function(i, value) {
				options += '<option value="' + value['name'] + '#'
						+ value['start'] + '#' + value['end'] + '#' +  value["chain"] + '">'
						+ value['name'] + '(' + value['start'] + ' -'
						+ value['end'] + ')' + '</option>';
			});
			$("select#feature").html(options);
			
			// Creo el nuevo glmol -> DEBE TENER LA INFO DE ANTES!
			
			
			
		}
	});
}

// END panelModel

function initGlmolPanelPresenter(pdbId, compositeId,on_end,residueMap) {
	console.info('Inicializando MVP');
	pdbParser = new PDBParser();
	
	
	glmolPanelM = new PDBPanelModel(pdbParser);
	glmolPanelM._residueMap = residueMap
	glmolPanelV = new PDBPanelView();
	glmolPanelP = new PDBPanelPresenter(glmolPanelM, glmolPanelV);
	glmolPanelM._pdbId = pdbId;
	glmolPanelM._compositeId = compositeId;
	glmolPanelP._model = glmolPanelM;
	glmolPanelP._view = glmolPanelV;
	
	glmolPanelM.on_end = on_end;
	glmolPanelP.init();
	
	return glmolPanelM
}

function destroyGlmolPanelPresenter() {
	console.info('Destruyendo MVP');

	$('#glmol_pdb').remove();
	$('#glmol_controls_panel').remove();
	$('.pocket_data_src').each(function() {
		$(this).remove();
	});
	$('.pocket_check').each(function() {
		$(this).remove();
	});
	$('.pocket_color').each(function() {
		$(this).remove();
	});
	$('#glmolPocketTable').remove();

	glmolPanelM = null;
	glmolPanelV = null;
	glmolPanelP = null;
}
