/**
 * gfhj
 */
$.PocketList = function(divElement, pdbId, pockets) {
	this.divElement = divElement;

	this.pockets = pockets;
	this.pocket_loaded_listener = null;
	this.pocket_toogle_listener = null;
	this.pocket_color_listener = null;
	this.pocket_strs = {};
	this.pocket_status = {}
	this.pocket_url = '../rest/structure/' + pdbId + '/pocket/'; // _pocketNumber
}

$.PocketList.prototype = {
	init : function() {
		// var ordered_pockets = this.pockets.sort(function(a, b) {
		// return b.properties["Drug Score"] - a.properties["Drug Score"];
		// });
		$.each(this.pockets,this.render_pocket.bind(this));

		this.configure_table();
	},
	registerHandlers : function() {
		this.registerPocketCheckEvent();
		this.registerPocketColorEvent();
	},

	onCheckToogle : function(evt) {		
		var srcElement = evt.currentTarget;
		var id = (evt.srcElement != null) ? srcElement.id : evt.currentTarget.id ;
		var pocketNumberStr = id.substring(id.lastIndexOf('_') + 1);
		
		if (typeof (this.pocket_strs[pocketNumberStr]) == "undefined") {
			this.load_pocket(pocketNumberStr);
		} else {
			this.pocket_status[pocketNumberStr] = srcElement.checked;
			this.pocket_toogle_listener.set_pocket_status(pocketNumberStr,
					this.pocket_status[pocketNumberStr]);
		}
		
	},
	onColorChanged : function(pocketNum, color) {
		this.pocket_color_listener.set_pocket_color(pocketNum, color);
	},
	registerPocketColorEvent : function() {
		this._colorSelector = $('#glmolPocketTable').find('.pocket_color');
		var pocket_list = this;
		this._colorSelector.each(function(index, elem) {
			$(elem).val('#' + $.fn.colorPicker.defaults.colors[index]);
			$(elem).change();
		});
		this._colorSelector.each(function() {

			var id = this.id;
			var pocketNumberStr = id.substring(id.lastIndexOf('_') + 1);

			pocket_list.pocket_color_listener.set_pocket_color(pocketNumberStr,
					$(this).val());

			$(this).change(function() {
				var val = $(this).val();
				pocket_list.onColorChanged(pocketNumberStr, val);
			});
		});
	},
	registerPocketCheckEvent : function() {
		this._colorChecks = this.divElement.find('.pocket_check');
		var pocket_list = this;
		
		this._colorChecks.each(function() {
			
			$(this).on('change', pocket_list.onCheckToogle.bind(pocket_list));
		});
	},
	call_pocket_loaded_listener : function(i, pocket_str) {
		// var pid = pocket_str.split("HEADER Information about the pocket")[1]
		// .split(":")[0].trim()
		// pid = (parseInt(pid) - 1).toString();
		this.pocket_strs[i] = pocket_str

		this.pocket_loaded_listener.pocket_str(i, pocket_str)

	},
	load_pocket : function(i) {

		$.get(this.pocket_url + i, function(pocket_str) {
			this.call_pocket_loaded_listener(i, pocket_str);
			this.pocket_toogle_listener.set_pocket_status(i, true);
		}.bind(this));

	},
	configure_table : function() {
		$('#glmolPocketTable').find('.pocket_color').each(
				function(i, colorPickerElem) {
					$(colorPickerElem).colorPicker();
					$(colorPickerElem).val(
							'#' + $.fn.colorPicker.defaults.colors[i]);
					$(colorPickerElem).change();
				});
		$('#glmolPocketTable').dataTable({
			"paging" : true,
			"info" : false,
			"lengthMenu" : [ [ 5, 10, 50, -1 ], [ 5, 10, 50, "All" ] ],
			"searching" : false,
			"order" : [ 1, 'desc' ]
		});
	},
	render_pocket : function(i, pocket) {
		if (pocket.druggability_score > 0.2) {
			$("#drug_table_body")
					.append(
							'<tr><td>'

									+ '<span style="float:left">'
									+ i
									+ '&nbsp;&nbsp;</span>'
									+ '<input class="pocket_check" type="checkbox" id="pocket_check_'
									+ i
									+ '" /> &nbsp;&nbsp;&nbsp;'
									+ '<label for="pocket_check_'
									+ i
									+ '" ">'
									
									+ '</label>'
									+ '<input id="color_'
									+ i
									+ '" name="color'
									+ i
									+ '" class="pocket_color" type="text" value="red"/>'
									
									+ '</td><td><div class="druggabilityScoreWrapper" title="'
									+ pocket.druggability_score
									+ '"><div class="percentageBar" style="width: 8%;"></div></div>'
									+ pocket.druggability_score
									+ '</td></tr>');
		}

	},
	clean : function() {
		
		this._colorChecks.each(function(index, colorCheckElem) {
			$(colorCheckElem).attr('checked', false);
			$(colorCheckElem).change();		
			var id = $(colorCheckElem).attr('id');
			
			var pocketNumberStr =  id.substring(id.lastIndexOf('_') + 1);
			this.pocket_status[pocketNumberStr] = false;
			this.pocket_toogle_listener.set_pocket_status(pocketNumberStr,false);
			
		}.bind(this));
		

		
		this._colorSelector.each(function(index, colorPickerElem) {
			$(colorPickerElem).val(
					'#' + $.fn.colorPicker.defaults.colors[index]);
			$(colorPickerElem).change();
		});
	}
}

$.StructOptionPanel = function(divElement, pdbId) {
	this.divElement = divElement;
	// this.pockets = pockets;
	this.view = null;
	this.clean_listeners = [];

}

$.StructOptionPanel.prototype = {
	init : function() {
		this.registerHetatmHandler();
		this.registerCsaHandler();
		this.registerFeatureHandler();
		this.registerPfamImportantResiduesHandler();
		this.registerProteinVisualizationHandler();
		this.registerPocketsVisualizationHandler();
		this.registerUpdateButtonHandler();
		this.registerClearButtonHandler();
		this.registerVMDButtonHandler();
	},
	registerHetatmHandler : function(hetatmHandler) {
		$('#hetatomCheck').on('ifToggled', function(event) {

			this.view.showHetatm($('#hetatomCheck')[0].checked);
		}.bind(this));
	},
	registerCsaHandler : function() {
		$('#csaCheck').change(function() {

			var val = $('#csaCheck').val();
			csaHandler.call(this, val);
		}.bind(this));
	},
	registerFeatureHandler : function(featureHandler) {
		$('#feature').change(function() {
			
			var val = $('#feature').val();
			if (val != null){
				this.view.showFeature(val);	
			}
			
		}.bind(this));
	},
	registerPfamImportantResiduesHandler : function(
			pfamImportantResiduesHandler) {

		$('#pfamCheck').change(function() {
			var val = $('#pfamCheck').checked;
			this.view.showPfamImportantResidues(val);
		}.bind(this));
	},
	// REGISTERS de combos-string
	registerProteinVisualizationHandler : function() {

		$('#proteinVisualization').change(function() {
			var val = $('#proteinVisualization').val();
			this.view.showProteinVisualizationHandler(val);
		}.bind(this));
	},
	registerPocketsVisualizationHandler : function() {

		$('#pocketsVisualization').change(function() {
			var val = $('#pocketsVisualization').val();
			this.view.showPocketsVisualizationHandler(val);
		}.bind(this));
	},
	// REGISTERS de botones
	registerUpdateButtonHandler : function() {
		$('#updateButton').click(function(e) {
			this.view.update();
		}.bind(this)).bind(this);
	},
	_on_clean_click : function(evt) {
		$.each(this.clean_listeners, function(y, x) {			
			x.clean();
		});

		$('#hetatomCheck').attr('checked', false); // No dispara
		// evento
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

		// Lanza el evento del otro botón (el handler lo mando
		// directo
		// al elem HTML, así que no tengo ref al mismo simil Action)
		$('#updateButton').click();
	},
	registerClearButtonHandler : function() {
		$('#clearButton').click(this._on_clean_click.bind(this));
	},
	registerVMDButtonHandler : function() {
		$('#vmdButton').click(function() {
			this.view.downloadVMD();
		}.bind(this));
	},
}
$.StructureViewer = function(divElement, pdb_id) {
	this.divElement = divElement;
	this.pdbParser = new PDBParser();
	this.pdbtxt = "";
	this.on_end = null;
	this.pdb_id = pdb_id;

	this._pocketsConf = {}; // mapa (associative array) con elems {active: t/f
	this._glmol_pockets = {}

	this.pdb_url = "../rest/structure/" + pdb_id + "/pdb";
	this.hetatom_url = '../rest/structure/' + pdb_id + "/heatatom";
	this.features_url = '../rest/structure/' + pdb_id + "/features";

	this._hetatm = true;
	this._csa = false;
	this._feature = false;
	this._pfamImportantResidues = false;

	this._proteinVisualization = "chain";
	this._pocketsVisualization = "atoms";
}

$.StructureViewer.prototype = {
	init : function(pdb_str){	

			this.pdbParser.parsePdb(pdb_str);
			this.pdb_txt = this.pdbParser.getCorrectedPdbString();
			this.pdbParser.buildPdbString(this.pdb_txt, [])
			
			if (this.on_start != undefined) {
					this.on_start();
				}
			
			
			this._glmol_pdb = new GLmol('glmol_pdb');
			
			this._glmol_pdb.loadMoleculeStr(null, this.pdb_txt);
			
			
			this.updateGlmol();

			if (this.on_end != undefined) {
				this.on_end();
			}
			
//			$.get(this.hetatom_url).success(function(heatom_str) {
//				this.load_heatom(heatom_str);
//				this.updateGlmol();
//
//				if (this.on_end != undefined) {
//					this.on_end();
//				}
//
//			}.bind(this)).fail(function() {
//				alert("Error downloading heatoms")
//			});
		
	},
	web_init : function() {
		//FIXME
		//this.cargarFeatures()
		$.get(this.pdb_url).success(this.init.bind(this)).fail(function() {
			alert("Error downloading pdb")
		});
	},
	load_heatom : function(heatom_str) {

		if (heatom_str.trim().length < 10) {
			$('#hetatom_section').remove();

		} else {
			this.pdbParser.addHetatm(heatom_str);
			this.pdb_txt = this.pdbParser.getCorrectedPdbString();
			this._glmol_pdb.loadMoleculeStr(null, this.pdb_txt);
		}
	},
	set_pocket_color : function(pocketNumber, color) {

		var pockConf = this._pocketsConf[pocketNumber];
		if (pockConf == undefined) {
			pockConf = {
				color : "",
				active : false
			};
			this._pocketsConf[pocketNumber] = pockConf;
		}
		pockConf.color = color;

	},
	set_pocket_status : function(pocketNumber, status) {

		var pockConf = this._pocketsConf[pocketNumber];
		pockConf.active = status;

	},
	pocket_str : function(pocketNumber, data) {
		var pockConf = this._pocketsConf[pocketNumber];
		if (pockConf == undefined) {
			pockConf = {
				active : false
			};
		}
		this._pocketsConf[pocketNumber] = pockConf;
		var newData = data.substring(0, data.indexOf('ATOM'));
		var pocketAtoms = this.pdbParser.getAtomsPDB(data);

		var extendedPocketAtoms = this.pdbParser.getPartnersAtomsInResidues(
				pocketAtoms, this.pdbParser.getResidueAtomsMap());
		var extra = this.pdbParser.getAtomsSubPDB(extendedPocketAtoms);
		newData = newData + extra;
		newData = newData
				+ data.substring(data.lastIndexOf('TER'), data.length);
		pockConf["data"] = newData;
	},
	setPocketColor : function(pocketNumber, color) {

		var pockConf = this._pocketsConf[pocketNumber];
		pockConf.color = color;
		this._pocketsConf[pocketNumber] = pockConf;
	},
	update : function() {
		this.updateGlmol();
	},
	updateGlmol : function() {

		// Dereferencio this, adentro de la función anónima cambia el this
		// (cambia
		// el contexto)
		var priv_model = this;

		var glmol_csa;
		var glmol_pfam;
		var glmol_features;

		for ( var pocketNumber in this._pocketsConf) {
			var pockConf = this._pocketsConf[pocketNumber];
			if (pockConf != undefined && pockConf.active) {
				this._glmol_pockets[pocketNumber] = new GLmol('glmol_pocket_'
						+ pocketNumber, true);
				this._glmol_pockets[pocketNumber].loadMoleculeStr(null,
						this._pocketsConf[pocketNumber]["data"]);
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
				this.drawBondsAsStick(asu, glmol_csa.getAllAtoms(), 0.2, 0.4,
						true, false, 0.3);
			}

			
			if (priv_model._feature) {
				debugger
				this.colorByAtom(glmol_features.getAllAtoms(), {});
				this.drawBondsAsStick(asu, glmol_features.getAllAtoms(), 0.2,
						0.4, true, false, 0.3);
			}

			if (priv_model._pfamImportantResidues) {
				this.colorByAtom(glmol_pfam.getAllAtoms(), {});
				this.drawBondsAsStick(asu, glmol_pfam.getAllAtoms(), 0.2, 0.4,
						true, false, 0.3);
			}

			// Revisar uso de features
			// if (pocketsConf.features) {
			// this.colorByAtom(glmol_features.getAllAtoms(), {});
			// this.drawBondsAsStick(asu, glmol_features.getAllAtoms(), 0.2,
			// 0.4,
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

						var misAlpha_pol = this.getResiduesById(
								alphaHetatm_pol, '   '
										+ (1 + parseInt(pocketNumber)));
						var misAlpha_apol = this.getResiduesById(
								alphaHetatm_apol, '   '
										+ (1 + parseInt(pocketNumber)));

						this.colorAtoms(misAlpha_apol, parseInt('0xD9D9D9'));
						this.colorAtoms(misAlpha_pol, parseInt('0x'
								+ pockConf.color.substring(1)));

						var misAlphaTotal = misAlpha_pol.concat(misAlpha_apol);
						;
						this.drawAtomsAsSphere(asu, misAlphaTotal, 2.0,
								2 * this.thickness);

					} else if (priv_model._pocketsVisualization == 'atoms') {

						if (pockConf.color != '#bbbbbb') {
							this.colorAtoms(
									priv_model._glmol_pockets[pocketNumber]
											.getAllAtoms(), parseInt('0x'
											+ pockConf.color.substring(1)));
							this
									.drawAtomsAsSphere(
											asu,
											priv_model._glmol_pockets[pocketNumber]
													.getAllAtoms(), 1.0,
											this.thickness);
						} else {
							this.colorByAtom(
									priv_model._glmol_pockets[pocketNumber]
											.getAllAtoms(), {});
							this.drawBondsAsStick(asu,
									priv_model._glmol_pockets[pocketNumber]
											.getAllAtoms(), 0.2, 0.4, true,
									false, 0.3);
						}
					} else {
						console
								.error('No se conoce la visualización de pockets '
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

	},

	showHetatm : function(val) {
		this._hetatm = val;
	},
	showPfamImportantResidues : function(val) {
		this._pfamImportantResidues = val;
	},

	// REGISTER de los combos (string)
	showProteinVisualizationHandler : function(val) {
		this._proteinVisualization = val;
	},
	showPocketsVisualizationHandler : function(val) {
		this._pocketsVisualization = val;
	},

	downloadVMD : function() {
		// El conocimiento del llamado al server y sus parámetros acá!

		var urlBase = '../rest/structure/' + this.pdb_id.toUpperCase()
				+ '/download';

		for ( var pocketNumber in this._pocketsConf) {
			var pockConf = this._pocketsConf[pocketNumber];
			if (pockConf != undefined && pockConf.active) {
				urlBase += '&pocketNumbers=' + pocketNumber;
			}
		}
		$('#vmdDownloadLink').attr('href', urlBase);
		window.location = urlBase;
	},
	cargarFeatures: function(features){
		var options = '<option value="">Select</option>';

		$.each(features, function(i, value) {
			options += '<option value="' + value['name'] + '#'
					+ value['start'] + '#' + value['end'] + '#'
					+ value["chain"] + '">' + value['name'] + '('
					+ value['start'] + ' -' + value['end'] + ')'
					+ '</option>';
		});
		$("select#feature").html(options);
	},
	web_cargarFeatures : function() {
		// Creo el nuevo glmol -> DEBE TENER LA INFO DE ANTES!
		$.get('../rest/structure/' + this.pdb_id + "/features").success(this.cargarFeatures.bind(this));
	},
	mapSequenceResidue : function(seqResNum, chain) {
		if (this._residueMap != null) {
			return this._residueMap[chain][seqResNum.toString()]
		} else {
			return seqResNum;
		}
	},
	showFeature : function(feature_str) {
		var featuresResidues = [];
		featureData = feature_str.split("#");
		var cadena = "";

		if (featureData.length > 3) {
			cadena = $.trim(featureData[3]);
		}
		for (var x = parseInt(featureData[1]); x <= parseInt(featureData[2]); x++) {
			position = this.mapSequenceResidue(x, cadena);
			featuresResidues.push({
				posicion : position,
				cadena : cadena
			});
		}

		this._feature = this.pdbParser.buildPdbString(this.pdb_txt,
				featuresResidues);
	}
}
