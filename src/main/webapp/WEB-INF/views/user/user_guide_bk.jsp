<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

		<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL" value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />

	<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>

<title>User Guide</title>


<style type="text/css">
p {
	font-size: 16px;
}

li {
	font-size: 16px;
}

figure {
	vertical-align: middle; <!--
	border: 1px solid #000-->;
	padding: 5px;
	margin-bottom: 20px;
	margin-top: 20px;
	text-align: center
}
</style>


</head>
<body class="skin-blue">

	<script type="text/javascript">
$("body").addClass("container")
$(".content-header").remove()
</script>


		<div class="row">
			<div class="col-md-12">
		

				<div>
					<ul>

						<li><a href="#genomes">Genome list</a><br /></li>
						<li><a href="#genome">Genome</a>
							<ul>
								<li><a href="#help_jbrowse">JBrowse</a><br /></li>
								<li><a href="#help_krona">Krona</a><br /></li>
							</ul></li>
						<li><a href="#tree_search">GO Tree</a><br /></li>
						<li><a href="#pathways">Pathways</a><br /></li>
						<li><a href="#search">Search </a><br /></li>
						<li><a href="#protein">Protein</a><br /></li>
						<li><a href="#structure">Structure</a><br /></li>
						<li><a href="#blast">BlastP</a><br /></li>
						<li><a href="#msa">Protein MSA</a><br /></li>
						<li><a href="#jobs">Job List</a><br /></li>
					</ul>

				</div>

		
				<figure>
					<img src="${baseURL}/public/html/html/screen_layout.png"
						alt="screen blocks layout" />
					<figcaption>Figura 3: Layout de la pantalla</figcaption>
				</figure>
				<br />
				<p>En la figura 3, se numeran 3 &#225;reas:
				<ul>
					<li>(1) <b>Header</b>: A la izquierda est&#225; el bot&#243;n para hacer
						colapsar/mostrar la <a href="#navbar">Barra de navegación</a> <img
						src="collapse_button.png" /> <br /> A la derecha está el usuario
						actualmente logueado y desde allí puede desloguearse. <img
						src="logout.png" />
					</li>
					<li>(2) <b>Contenido</b>: En esta área se verán los datos /
						herramientas que el usuario consultará / utilizara. Ej: Proteína,
						Genoma, Búsquedas, etc
					</li>
					<li>(3) <b id="navbar">Barra de navegación</b>: accesos
						directos a las distintas partes de la aplicación . Cuando la
						página no entre bien en la pantalla el panel se colapsara
						automaticamente.
					</li>
				</ul>
				</p>
				<p>De aquí en adelante las pantallas mostrarán solo la sección
					de contenido</p>

				

				<h2 id="genomes">Genomas</h2>
				<p>Para ver los genomas disponibles, se puede ir desde el
					dashboard o desde la barra de navegación</p>
				<figure>
					<img src="${baseURL}/public/html/genomes.png" alt="Genomes list screen" />
					<figcaption>Figura 6: Lista de genomas</figcaption>
				</figure>
				<br />
				<p>
				<ul>
					<li>(1) Cantidad de genomas que se visualizan en la tabla</li>
					<li>(2) Para Filtrar a los genomas por nombre</li>
					<li>(3) Página de la búsqueda: cuando la cantidad de genomas
						visualizados es menor a la cantidad que hay disponibles</li>
					<li>(4) Tabla de genomas. En el título se puede ordenar por
						columna. Haciendo click sobre el nombre del organismo se accede a
						su información</li>
				</ul>
				</p>


				<h2 id="genome">Genoma / Organismo</h2>
				Se puede llegar a la pantalla de un genoma desde distintos lados:
				desde la lista de genomas (pantalla anterior), desde la proteína,
				gen, búsqueda, etc. En esta sección se muestra toda la información
				general y se accede a todas las búsquedas disponibles.
				<figure>
					<img src="${baseURL}/public/html/genome1.png"
						alt="Genome screen. first half" />
					<figcaption>Figura 7: Pantalla del Genoma, primera
						parte</figcaption>
				</figure>
				<br />
				<p>
				<ul>
					<li>(1) Nombre del genoma</li>
					<li>(2) Genome browser, para navegar el genoma. Se utiliza el
						componente <a href="#jbrowse">JBrowse</a>
					</li>
					<li>(3) Descarga de datos: fasta y gff3</li>
					<li>(4) Panel de búsquedas. El objetivo es filtrar los
						productos génicos por distintos criterios. Las opciones son: por
						palabra clave, por nombre de gen, por término de gene ontology,
						por pathways o por score de drogabilidad (en desarrollo...)</li>
					<li>(5) Cantidad de productos génicos del genoma que tienen
						una determinada anotación, por ejemplo hay 133 proteinas que
						tienen péptido señal</li>
				</ul>
				</p>

				<figure>
					<img src="${baseURL}/public/html/genome2.png"
						alt="Genome screen. second half" />
					<figcaption>Figura 8: : Pantalla del Genoma, segunda
						parte</figcaption>
				</figure>
				<br />
				<p>
				<ul>
					<li>(1) Distribución de drogabilidad a nivel estructura. Se
						obtiene en base a las proteinas que tienen estructura. Con el
						programa fpocket se calculan los pockets drogables y se toma el
						pocket con mayor druggability score de cada proteina.</li>
					<li>(2) Tabla de estadísticas de drogabilidad</li>
					<li>(3) Visualizador general de distribución de anotaciones de
						GO y EC. Utiliza el componente <a href="#krona">Krona</a>

					</li>

				</ul>
				</p>


				<h3 id="help_jbrowse">JBrowse</h3>
				El JBrowse es una navegador de genomas. En el mismo se carga el
				genoma de referencia y varios "tracks", donde se ubica la
				información anotada a lo largo de la secuencia. Por ahora solo se
				cargan los genes.
				<figure>
					<img src="${baseURL}/public/html/jbrowse.png" alt="Jbrowse" />
					<figcaption>Figura 9: JBrowse Navegador de Genoma</figcaption>

				</figure>
				<ul>
					<li>(1) Barra de navegación: Esta botonera sirve para
						desplazarse sobre la secuencia seleccionada (cromosomas,
						transcriptos o contigs) Haciendo zoomIn o zoomOut se puede ver la
						secuencia o la densidad génica respectivamente. En la lista
						desplegable se elige la secuencia de referencia. En la imagen está
						seleccionada la NC_000962.3</li>
					<li>(2) Porción de Secuencia Visible: con el formato
						"secuencia:posición_inicio..posición_fin" se puede ver o cambiar
						la porción del genoma que se está visualizando en ese momento.</li>
					<li>(3) Referencia: se visualiza el detalle de la secuencia.
						Las distintas bases están representadas con distintos colores y
						haciendo zoom in se pueden visualizar los nombres.</li>
					<li>(4) Genes: Track donde se visualizan los genes. Con click
						izquierdo se navega hasta la proteína. Con click derecho se abre
						un menu para visualizar la información del gen que contiene el
						GFF.</li>
				</ul>
				<br />


				<h3 id="help_krona">Krona</h3>
				En este control se puede visualizar la distribución de anotaciones
				de <a href="http://enzyme.expasy.org/">EC</a> y de <a
					href="http://geneontology.org/"> GO </a> sobre las proteinas del
				genoma que pudieron ser anotadas. Las anotaciones consisten en <b>términos</b>
				de una ontología, aplicados a una o más proteinas. Los <b>terminos</b>
				están ordenados jerárquicamente y tener un término hijo implica
				tener a sus predecesores. Por ejemplo una kinasa implica que se es
				una Transferasa. La información se navega por términos (siguiendo
				los niveles de la ontología) y el nivel raíz es el centro. Haciendo
				doble click sobre las porciones del gráfico, se entra en ese nivel y
				se ven solo las categorías "hijas". Por ejemplo, si estando en EC se
				hace doble click sobre "Transferases", se ocultan las categorías de
				Hidrolases, Oxoreductases, Ligases, Lysases e Isomerases y se
				muestran, ocupando la totalidad de la torta los tipos de
				transferasas: Acetiltranferases, Phosphorous Transferases, etc ...
				Para volver al nivel superior hay que hacer click sobre la categoría
				padre en el círculo interior del grafico.

				<figure>
					<img src="${baseURL}/public/html/krona.png" alt="krona graph" />
					<figcaption>Figura 10: Krona Control Interactivo</figcaption>

				</figure>
				<br />
				<ul>
					<li>(1) Seleccion de Ontologia: EC, GO:Biological_Process,
						GO:Celular_component y GO:Mollecular_Function</li>
					<li>(2) Detalles del término: Una vez que se hace click sobre
						un termino, los detalles a visualizar son: el nombre, el cual es
						un link a la lista de proteinas anotadas con ese termino, la
						cantidad de las mismas. y el porcentaje en el total que
						representan.</li>
					<li>(3) Cuando un término tenga muy pocas proteinas asignadas,
						la proporción en el gráfico será muy chica para verse
						correctamente. Cuando eso pasa, en este sector de la página, se
						muestran en este área.</li>
				</ul>

				<h3 id="tree_search">Buscar genes por árbol de GO</h3>
				<p>
					Esta pantalla provee otra forma de visualizar la navegación por
					terminos de GO. Se llega solo desde la pantalla del genoma.
					Estructura los términos como un árbol y permite ver sus terminos
					"hijos"<img src="${baseURL}/public/html/tree_folder.png" /> (se ve el
					nombre, código del termino y cantidad de proteinas anotadas con él)
					y productos génicos anotados.<img
						src="../widgets/jstree/themes/default/geneprod.ico" />. Este
					último aparece como hijo del término más específico y haciendo
					click derecho sobre él, se puede navegar al mismo. De la misma
					forma haciendo click derecho sobre un término, se puede navegar a
					la lista de proteinas que están anotadas con él.
				</p>
				<figure>
					<img src="${baseURL}/public/html/tree_search.png"
						alt="Gene Ontology Term Tree Search" />
					<figcaption>Figura 11: Árbol de GO</figcaption>

				</figure>
				<br />
				<ul>
					<li>(1) Lista de genes. Cuando se hace click derecho se abre
						el menú contextual que permite navegar hacia la proteína.</li>
					<li>(2) Barra de Herramientas: en ella se puede
						buscar(aparecen las ramas de los términos que tienen esa palabra,
						limpiar (vuelve la búsqueda a 0)) o refrescar la pantalla.</li>
				</ul>



				<h3 id="pathways">Pathways</h3>
				<p>
					Aquí se listan los pathways del organismo (si la información está
					cargada). Los pathways se representan como grafos, donde los nodos
					son proteinas, catalizan una determinada reacción y las aristas son
					los substratos/reactantes, que conectan 2 proteinas. Es decir,
					cuando el producto de una reacción producido por una proteína X es
					reactante de otra reacción producida por una proteína Y, X e Y
					están conectados por una arista. Los pathways y reacciones
					utilizados son los de la base. <a href="http://biocyc.org/">BioCyc</a>.
					Durante la anotación deben eliminarse o no conectar proteinas que
					tengan especies muy comunes, como ser H2O. A continuación se
					muestra la pantalla en 2 figuras distintas. La visualización del
					grafo se hizo con <a href="http://js.cytoscape.org/">Cytoscape
						JS</a> y por ahora muestra una distribución de nodos por defecto.
				</p>
				<figure>
					<img src="${baseURL}/public/html/pathways1.png" alt="pathways screen" />
					<figcaption>Figura 12: Seleccion de pathway/s</figcaption>



				</figure>
				<br />

				<ul>
					<li>(1) Lista de pathways: en esta tabla se muestran todos los
						pathways que fueron identificados en el organismo.
						<ul>
							Haciendo click sobre:
							<li><small title="click to go to Biocyc"
								class="badge bg-yellow">?</small> se navega al termino en la
								pagina de Biocyc.</li>
							<li>El pathway se va a la lista de proteinas que se
								encuentran dentro de ese pathway</li>
							<li>El checkbox de selección, se agrega el pathway a la
								lista para ser visualizados más abajo (3)</li>
						</ul>
					</li>
					<li>(2) Una vez que se eligieron los pathways a visualizar, se
						debe hacer click sobre "Reload selected Pathways" para que se
						actualiza el grafo (3). "Redraw" muestra el grafo entero, como un
						zoom out al máximo</li>
					<li>(3)Gráfico: Se puede ver en azul el Nodo -> Reacción y en
						rojo la especie que conecta 2 proteinas. Se hace zoom in y zoom
						out con la ruedita del mouse y se traslada manteniendo apretado el
						click derecho. Cuando se hace click sobre un nodo, se puede
						visualizar claramente con quien está directamente conectado. Para
						salir de dicha visualización, hay qué hacer click sobre otra zona
						del gráfico.</li>

				</ul>

				<figure>
					<img src="${baseURL}/public/html/pathways2.png" alt="pathways screen" />
					<figcaption>Figura 13: Visualización de pathway/s</figcaption>

				</figure>
				<br />

				<ul>
					En esta parte de la pantalla se ve la tabla con las proteinas que
					catalizan las reacciones pertenecientes a alguno de los pathways
					seleccionados más arriba.
					<li>(1) Haciendo click sobre el nombre de la proteína se
						navega a la pantalla de la misma.</li>
					<li>(2) Para ver el detalle de la reacción en BioCyc</li>
					<li>(3) Para en otra pantalla filtrar las proteinas que
						realizan esta reacción</li>
					<li>(4) Para agregar a la proteína a la lista de <a
						href="#msa"> alineamiento multiple </a></li>
				</ul>

				<h2 id="search">Busqueda Principal</h2>
				<p>En esta pantalla se pueden buscar productos génicos (por
					ahora proteinas) por palabra clave y es donde apuntan las otras
					paginas para realizan filtros. Por ejemplo, si se elige la lista de
					proteinas de un determinado pathway, el resultado se muestra aqui.
					Una palabra está asociada con un producto génico cuando la misma
					este: en su descripcion, su nombre, su gen o en alguna de las
					anotaciones (ontologias, familia, metabolismo) o su descripcion.
					Por ejemplo si está la palabra "dna" en la descripcion de un
					dominio PFAM que tiene anotada una determinada proteína X, dicha
					proteína tendrá asociada la palabra "dna" Tener en cuenta que no
					busca palabras parciales y todas son en inglés.</p>
				<figure>
					<img src="${baseURL}/public/html/search.png" alt="search screen" />
					<figcaption>Figura 14: Tabla de Búsqueda</figcaption>


				</figure>
				<br />

				<ul>
					<li>(1) Borra todos los criterios de búsqueda</li>
					<li>(2) Con este primer control, se busca por palabra "libre".
						Al terminar de escribir, se debe oprimir "enter" para que se
						realice la búsqueda. Se puede buscar por mas de una palabra, y se
						mostrará la lista de productos que posean asociadas todas las
						palabras (criterio AND)</li>
					<li>(3) Esta búsqueda es más restringida que la anterior, ya
						que solo se pueden ingresar nombres de GO e EC. Para no tener que
						recordar los nombres, tiene incorporada una función de
						autocomplete.</li>
					<li>(4) En la cabecera de la tabla, a parte de estar los
						nombres de las columnas, hay otros filtros por palabra,
						específicos de la columna. Para que se refresque la búsqueda se
						debe apretar "enter".</li>
					<li>(5) Para agregar a la proteína a la lista de <a
						href="#msa"> alineamiento multiple </a></li>
					<li>(6) Para navegar a los detalles de la proteina</li>
					<li>(7) Para ir a la pantalla del genoma.</li>
					<li>(8) Clickeando sobre <i class="fa  fa-gears">&#160;</i> se
						muestra el pathway en la pantalla de Pathways
					</li>
					<li>(9) Para navegar a los detalles del gen</li>

				</ul>

				<h2 id="protein">Proteina</h2>
				<p>Aquí se muestran los detalle de la proteína.</p>
				<figure>
					<img src="${baseURL}/public/html/protein1.png" alt="protein overview" />
					<figcaption>Figura 15: Protein Screen part 1</figcaption>
					<ul>
						<li>(1) Panel de información general de la proteína</li>
						<li>(2) Link al Genoma</li>
						<li>(3) Link al Gen (posición de la proteína en el genoma )</li>
						<li>(4) Lista de estructuras asociadas a la proteína, y a sea
							modelos o homologas al pdb.
							<ul>
								Las columnas son:
								<li>Nombre de la estructura</li>
								<li>Templado utilizado en el caso de que sea una estructura
									calculada por homologia. La estructura del templado es
									pdb_cadena_cominenzo_fin. Comienzo y fin son los numeros de
									residuos (RESID) entre los cuales está alineada la
									subsecuencia, o sea la parte del segmento del pdb alineada con
									la proteina. Si los numeros son -1_-1, quiere decir que no se
									cuenta con el alineamiento de esa estructura</li>
								<li>Subsecuencia: parte de la proteína alineada/cubierta
									por la estructura</li>
								<li>Dominio de pfam, en el caso de que para realizar el
									alineamiento se haya utilizado dominios y no secuencias
									completas</li>
								<li>Drogabilidad: Druggability Score maximo de la
									estructura utilizando Fpocket</li>
							</ul>
						</li>
						<li>(5) El nombre de la estructura es un link para explorar
							la misma.</li>
					</ul>
				</figure>
				<br />
				<figure>
					<img src="${baseURL}/public/html/go_graph.png"
						alt="protein gene ontology graph" />
					<figcaption>Figura 16: Panel Gráfo de GO de la
						Proteina</figcaption>
					<ul>
						Este panel muestra las anotaciones de GO que tiene la proteína y
						sus relaciones entre sí. Solo se representan las relaciones "is_a"
						<li>(1) Para ver sólo los términos de una determinada rama de
							GO, se selecciona la misma en la lista desplegable y se clickea
							el botón "Redraw"</li>
						<li>(2) Los nodos rojos son las raíces de GO (BP, CC, MF)</li>
						<li>(3) Los nodos verdes el término anotado (hojas del
							grafo), que debería ser el término más específico.</li>
					</ul>

				</figure>
				<br />

				<figure>
					<img src="${baseURL}/public/html/reactions.png"
						alt="protein pathways reactions" />
					<figcaption>Figura 17: Panel de reacciones químicas</figcaption>
					<ul>
						En el panel de reacciones, se visualizan las mismas, agrupadas por
						pathway. Dentro de la reacción, se ven lo productos y substratos,
						que a continuación, tienen a las proteinas que se vinculan a
						partir de ellas. Por ejemplo en la reacción GAPOXNPHOSPHN, toma el
						sustrato GAP, que es producido por Rv1449c, Rv2858c, etc y produce
						DPG, que es consumido por Rv3837c, Rv3214, etc..
						<li>(1) Con el <i class="fa fa-filter">-</i> se abre la
							pantalla de búsqueda con lista de proteinas de ese pathway
						</li>
						<li>(2) Con <small title="click to go to Biocyc"
							class="badge bg-yellow">?</small> se abre el detalle del pathway
							en BioCyc
						</li>
						<li>(3) Clickeando sobre la proteína conectada, se navea al
							detalla de la misma.</li>
					</ul>
				</figure>
				<br />

				<figure>
					<img src="${baseURL}/public/html/sequence.png"
						alt="protein sequence features" />
					<figcaption>Figura 18: Panel de anotaciones de
						secuencia</figcaption>
					<ul>
						En esta tabla se marcan todas las anotaciones que mapean contra
						uno o mas aminoacidos. Estan agrupadas por Dominio, sitio
						catalítico, de unión y homología.

						<li>(1) Con <small title="click to go to Biocyc"
							class="badge bg-yellow">?</small> se abre el detalle del término
							en la página externa que corresponda.
						</li>
						<li>(2) Para visualizar los aminoácidos se ese feature en la
							secuencia (7)</li>
						<li>(3) Para ir a la herramienta blast importando la
							secuencia de la proteína. Si se selecciona una porción de la
							proteína la misma aparecerá en el control al lado del boton
							"Blast"</li>
						<li>(4) Cambiar el largo de la línea de la secuencia que se
							esta visualizando</li>
						<li>(5) Para agregar a la proteína a la lista de <a
							href="#msa"> alineamiento multiple </a></li>
						<li>(6) Cambia el formato de la visualización de la secuencia</li>
						<li>(7) Subsecuencia seleccionada en (2)</li>
					</ul>
				</figure>
				<br />


				<h2 id="structure">Estructura</h2>
				<p>
					En esta pantalla se visualiza una estructura atomica, perteneciente
					a una proteína, o una subsecuencia de la misma. La estructura puede
					ser un modelo calculado por homología o un cristal de <a
						href="http://www.rcsb.org/">PDB</a> El visualizador de moléculas
					utilizado es el <a href="http://webglmol.osdn.jp/index-en.html">GLMol</a>

				</p>

				<figure>
					<img src="${baseURL}/public/html/structure1.png"
						alt="structure screen part 1" />
					<figcaption>Figura 19: Estructura</figcaption>
					<ul>
						<li>(1) Molécula visualizada con GLmol</li>
						<li>(2) Propiedades de la estructura, en este caso tiene
							cargada solo una: Zmean: -1.43</li>
						<li>(3) Botonera: Los botones azules son listas de
							información asociadas con la estructura, como ser datos de CSA,
							detalle de los pockets, ligandos, etc. El boton download <a
							href="downloaded_structures"> descarga un zip </a> con el archivo
							pdb y otros con el resultado de la corrida con fpocket, para su
							fácil carga en el VMD.
						</li>
						<li>(4) Lista de Cadenas: en este panel se listan las cadenas
							y hay una fila para ligandos / hetatoms. Con el checkbox de la
							derecha se visualizan u ocultan los átomos de las cadenas, con la
							lupa se centra la vista en el conjunto de átomos que la componen,
							con la lista desplegable se cambia el estilo de visualización y
							haciendo click sobre el color se habilita la opción de cambiarlo.
							Este funcionamiento es análogo para el resto de los paneles.</li>
						<li>(5) En este panel se listan los pockets cuyo druggability
							score es mayor al 0.2. Se pueden visualizar los átomos que lo
							componen o las alpha spheres definidas por ellos.</li>
						<li>(6) La lista de features son anotaciones a nivel
							secuencia (dominio, cisteínas) o estructura (calculadas con
							criterios espaciales, como ser cisteínas libres o residuos que
							unen a drogas).</li>

					</ul>
				</figure>
				<br />
				<figure>
					<img src="${baseURL}/public/html/structure2.png"
						alt="structure screen part 2" />
					<figcaption>Figura 20: Ir al templado del modelo</figcaption>
					<ul>
						Para acceder al template con el que se generó un modelo:
						<li>(1) Se hace click sobre "PDB Templates". Lo que despliega
							una tabla en el medio de la pantalla</li>
						<li>(2) Luego sobre el vínculo se puede ir a la estructura
							determinada experimentalmente.</li>
					</ul>
				</figure>
				<br />


				<h2 id="blast">BlastP</h2>
				<p>
					Se puede hacer una busqueda de proteinas utilizando el algoritmo
					blast. El blast se ejecuta sobre las proteinas de todos los genomas
					del usuario. Cuando se accede desde la <a href="#navbar">Barra
						de navegación</a>, hay que ingresar la secuencia a buscar a mano. Si
					se hace desde el <a href="#seqpanel">panel de la secuencia</a> de
					la proteína, la misma se visualizará en la pantalla.
				</p>
				<figure>
					<img src="${baseURL}/public/html/blast.png"
						alt="structure screen part 1" />
					<figcaption>Figura 21: Pantalla de estructura</figcaption>
					<ul>
						<li>(1) Botonera: Se puede hacer blast sobre toda la
							secuencia o sobre la secuencia seleccionada, donde se muestra el
							inicio y fin se dicha selección en los 2 cuadros de texto. El
							botón para hacer blast en Uniprot, nos dirige a dicha pagina y
							sus resultados no están integrados con Patho-Gen</li>
						<li>(2) Selección de subsecuencia: en este control se puede
							marcar con el mouse, manteniendo apretado el botón izquierdo.
							Este panel es visible solamente si se esta realizando el blast
							desde el <a href="#seqpanel">panel de la secuencia</a>. También
							se respetará la subsecuencia seleccionada.
						</li>
						<li>(3) Secuencia editable que finalmente se buscará en la
							base de datos</li>
						<li>(4) Selección de la base de datos sobre la cual se
							realizará la búsqueda. Por ahora solo se puede sobre los genomas
							propios</li>
						<li>(5) Lista de parámetros modificables del algoritmo</li>
						<li>(6) Tabla de Resultados</li>
						<li>(7) Para agregar a la proteína a la lista de <a
							href="#msa"> alineamiento multiple </a></li>
						<li>(8) Un click sobre el link azul nos dirige a la pantalla
							de detalle de la proteína</li>

					</ul>
				</figure>
				<br />


				<h2 id="msa">Multiple Sequence Aligmnent</h2>
				Esta herramienta permite realizar un alineamiento múltiple de las
				secuencias proteicas dentro de la base de datos utilizando el
				programa <a href="http://www.ebi.ac.uk/Tools/msa/clustalo/">
					Clustal Omega </a>. Las secuencias están disponibles para esta
				operacion, a medida que se van agregando a la lista de alineamiento
				múltiple. La operacion se hace marcando un checkbox, indicado con el
				icono <i title="select for multiple aligment"
					class="fa fa-align-center">&#160;</i>, y se puede hacer desde: la
				pantalla de proteína, la pantalla de búsqueda, los resultados del
				blast y la pantalla de pathways.
				<figure>
					<img src="${baseURL}/public/html/msa.png"
						alt="Multiple Sequence Aligmnent" />
					<figcaption>Figura 22: Alineamiento multiple de
						secuencias Tool</figcaption>

				</figure>
				<br />

				<ul>
					<li>(1) El botón "Run Alignment" ejecuta el alineamiento sobre
						el fasta (3) generado a partir de la lista de proteinas (2). El
						botón "Delete Unselected" quita del fasta (3) las proteinas que en
						(2) no tiene su checkbox activo.</li>
					<li>(2) Lista de proteinas agregadas para el alineamiento
						multiple.</li>
					<li>(3) Fasta (editable) de proteinas a ser alineado, generado
						a partir de la lista de proteinas seleccionadas</li>
					<li>(4) Resultados del alineamiento, se puede descargar en
						formato fasta.</li>
				</ul>

				<h2 id="jobs">Jobs</h2>
				En esta pantalla están los resultados de las búsquedas realizadas
				con blast o los alineamientos múltiples (se borran pasado un
				determinado tiempo).
				<figure>
					<img src="${baseURL}/public/html/jobs.png" alt="Job list" />
					<figcaption>Figura 23: Lista de Trabajos</figcaption>
				</figure>
				<br /> En la tabla se visualiza la fecha, el tipo de proceso, en
				que estado está y haciendo click sobre los links de la columna ID se
				accede al resultado del procesamiento.

			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<h1 id="faq">FAQ</h1>
				<div>
					<h3 id="downloaded_structures">¿Qué archivos descargo en el
						zip de estructura?</h3>
					<p>El zip se arma con el ouput del programa fpocket:</p>
					<ul>
						<li>(file)_out.pdb: Achivo PDB, con las alpha spheres
							calculadas por fpocket</li>
						<li>(file)_info.txt: Propiedades de los pockets</li>
						<li>(file).tcl: Archivo de configuración para su
							visualización en el <a
							href="http://www.ks.uiuc.edu/Research/vmd/"> VMD </a>
						</li>
						<li>(file)_VMD.sh: Ejecutable para abrir el archivo con <a
							href="http://www.ks.uiuc.edu/Research/vmd/"> VMD </a> y las
							configuraciones en el .tcl
						</li>
					</ul>
					<p>Por ejemplo si se descarga la estructura 2cxc, se descargan
						los archivos: pdb2cxc_info.txt, pdb2cxc_out.pdb, pdb2cxc.tcl y
						pdb2cxc_VMD.sh</p>
				</div>
				<div>
					<h3 id="download_protein_list">¿Cómo subir propiedades a las
						proteinas?</h3>
					
					<div class="form-group">
										<p>The file must be a table, with columns separated by tab and UTF-8 enconding. There is one mandatory field
											called <b>id</b>, with the gene locus or Uniprot code. There
											are 2 types of columns, "values" or "numeric". Value columns
											must have no more than 20 different values. Numeric columns
											must have "," as decimal separator and no other simbol. To
											represent the absence of value in a numeric column, the
											string "NaN" must be used.</p>
										<label for="in_new_trascript">Example</label>
										<table class="table">
											<tr>
												<td>id</td>
												<td>essential</td>
												<td>chokepoint</td>
												<td>stress fold change</td>
											</tr>
											<tr>
												<td>Rv0064</td>
												<td>yes</td>
												<td>product</td>
												<td>0.3</td>
											</tr>
											<tr>
												<td>Rv1363</td>
												<td>no</td>
												<td>no</td>
												<td>6</td>
											</tr>
											<tr>
												<td>Rv1364</td>
												<td>no</td>
												<td>unknown</td>
												<td>NaN</td>
											</tr>

										</table>
									</div>
					
					
				</div>

			</div>
		</div>

</body>

	</html>
</jsp:root>
