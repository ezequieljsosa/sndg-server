<script src="https://d3js.org/d3.v4.min.js"><!--

//--></script>
<script type="text/javascript" src="/patho/public/widgets/nextprot/feature-viewer.nextprot.js">
<!--

//-->
</script>
<div id="div2"></div>
<script>
//Create a new Feature Viewer and add some rendering options
var ft2 = new FeatureViewer("LADSRQSKTAASPSPSRPQSSSNNSVPGAPNRVSFAKLREPLEVPGLLDVQTDSFEWLIGSPRWRESAAERGDVNPVGGLEEVLYELSPIEDFSGSMSLSFSDPRFDDVKAPVDECKDKDMTYAAPLFVTAEFINNNTGEIKSQTVFMGDFPMMTEKGTFIINGTERVVVSQLVRSPGVYFDETIDKSTDKTLHSVKVIPSRGAWLEFDVDKRDTVGVRIDRKRRQPVTVLLKALGWTSEQIVERFGFSEIMRSTLEKDNTVGTDEALLDIYRKLRPGEPPTKESAQTLLENLFFKEKRYDLARVGRYKVNKKLGLHVGEPITSSTLTEEDVVATIEYLVRLHEGQTTMTVPGGVEVPVETDDIDHFGNRRLRTVGELIQNQIRVGMSRMERVVRERMTTQDVEAITPQTLINIRPVVAAIKEFFGTSQLSQFMDQNNPLSGLTHKRRLSALGPGGLSRERAGLEVRDVHPSHYGRMCPIETPEGPNIGLIGSLSVYARVNPFGFIETPYRKVVDGVVSDEIVYLTADEEDRHVVAQANSPIDADGRFVEPRVLVRRKAGEVEYVPSSEVDYMDVSPRQMVSVATAMIPFLEHDDANRALMGANMQRQAVPLVRSEAPLVGTGMELRAAIDAGDVVVAEESGVIEEVSADYITVMHDNGTRRTYRMRKFARSNHGTCANQCPIVDAGDRVEAGQVIADGPCTDDGEMALGKNLLVAIMPWEGHNYEDAIILSNRLVEEDVLTSIHIEEHEIDARDTKLGAEEITRDIPNISDEVLADLDERGIVRIGAEVRDGDILVGKVTPKGETELTPEERLLRAIFGEKAREVRDTSLKVPHGESGKVIGIRVFSREDEDELPAGVNELVRVYVAQKRKISDGDKLAGRHGNKGVIGKILPVEDMPFLADGTPVDIILNTHGVPRRMNIGQILETHLGWCAHSGWKVDAAKGVPDWAARLPDELLEAQPNAIVSTPVFDGAQEAELQGLLSCTLPNRDGDVLVDADGKAMLFDGRSGEPFPYPVTVGYMYIMKLHHLVDDKIHARSTGPYSMITQQPLGGKAQFGGQRFGEMECWAMQAYGAAYTLQELLTIKSDDTVGRVKVYEAIVKGENIPEPGIPESFKVLLKELQSLCLNVEVLSSDGAAIELREGEDEDLERAAANLGINLSRNESASVEDLA","#div2", {
    showAxis: true,
    showSequence: true,
    brushActive: true,
    toolbar:true,
    bubbleHelp:true,
    zoomMax:10
            });

//Add some features
ft2.addFeature({
    data: [{x:53,y:64},{x:67,y:77},{x:84,y:93},{x:118,y:124}
    ,{x:233,y:240},{x:244,y:252}],
    name: "helix",
    className: "test1",
    color: "#005572",
    type: "rect",
    filter: "type1"
});
ft2.addFeature({
    data: [{x:141,y:145},{x:192,y:195}],
    name: "turn",
    className: "test2",
    color: "#006588",
    type: "rect",
    filter: "type2"
});
ft2.addFeature({
    data: [{x:431,y:501}],
    name: "PF04565.11",
    className: "test3",
    color: "#eda64f",
    type: "rect",
    filter: "type2"
});
ft2.addFeature({
    data: [{x:509,y:576}],
    name: "PF10385.4",
    className: "test4",
    color: "#F4D4AD",
    type: "rect",
    height: 8,
    filter: "type1"
});
ft2.addFeature({
    data: [{x:368,y:425}],
    name: "4kbj_A_369_425",
    className: "test4",
    color: "#F4D4AD",
    type: "rect",
    height: 8,
    filter: "type1"
});

ft2.addFeature({
    data: [{x:445,y:446},{x:450,y:451}],
    name: "test feature 3",
    className: "variant",
    color: "rgba(0,255,154,0.3",
    type: "unique",
    filter: "Variant"
});

var dataDemo = [];
for (var i=1;i<100;i++) {
    var count = Math.floor((Math.random() * 20) + 1);
    dataDemo.push({
        x: i*2,
        y:count
    })
}

ft2.addFeature({
    data: dataDemo,
    name: "test feature 5",
    className: "test5",
    color: "#008B8D",
    type: "line",
    filter: "type2",
    height: "5"
});

//Beside positions of each element, you can also give a specific description, which will appears in the tooltip when mouse hover, and a specific ID, for example to link a click event on the feature with something else in your project.
ft2.addFeature({
    data: [{x:120,y:154,description:"aaaaa",id:"a1"},{x:22,y:163,description:"bbbbb",id:"b1"},
           {x:90,y:108,description:"ccccc",id:"c1"},{x:10,y:25,description:"ddddd",id:"d1"},
           {x:193,y:210,description:"eeeee",id:"e1"},{x:78,y:85,description:"fffff",id:"f1"},
           {x:96,y:143,description:"ggggg",id:"g1"},{x:14,y:65,description:"hhhhh",id:"h1", color:"#12E09D"},
           {x:56,y:167,description:"jjjjj",id:"j1"}],
    name: "test feature 6",
    className: "test6",
    color: "#81BEAA",
    type: "rect",
    filter: "type2"
});

</script>