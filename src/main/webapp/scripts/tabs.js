var bI111=0,bb111=0,b11b1=0,I1b=0,IbII=0,Ib1b1=0,IIIb1=0,I11=0,IbbI1=0,bIbb=0,I1I1=0,bb=0,b1=[],I1b11=false,bI11=0,Ib11=null,s="",I111b=2000,b111;bbIIb();function bbIIb(){var a=navigator.userAgent,n=navigator.appName,v=navigator.appVersion;bIbb=v.indexOf("Mac")>=0;IbbI1=document.getElementById?1:0;var III1b=(parseInt(navigator.productSub)>=20020000)&&(navigator.vendor.indexOf("Apple Computer")!=-1);if(III1b&&navigator.product=="Gecko"){b11b1=1;I11=6;return;};if(a.indexOf("Opera")>=0){IbII=1;I11=parseFloat(a.substring(a.indexOf("Opera")+6,a.length));}else if(n.toLowerCase()=="netscape"){if(a.indexOf("rv:")!=-1&&a.indexOf("Gecko")!=-1&&a.indexOf("Netscape")==-1){IIIb1=1;I11=parseFloat(a.substring(a.indexOf("rv:")+3,a.length));}else{b11b1=1;if(a.indexOf("Gecko")!=-1&&a.indexOf("Netscape")>a.indexOf("Gecko")){if(a.indexOf("Netscape6")>-1)I11=parseFloat(a.substring(a.indexOf("Netscape")+10,a.length));else if(a.indexOf("Netscape")>-1)I11=parseFloat(a.substring(a.indexOf("Netscape")+9,a.length));}else I11=parseFloat(v);};}else if(document.all?1:0){bI111=1;I11=parseFloat(a.substring(a.indexOf("MSIE ")+5,a.length));};I1b=b11b1&&I11<6;bb111=bI111&&I11>=5;Ib1b1=IbII&&I11>=7;I1I1=bI111||Ib1b1;};function bIbIb(){var bbbbb=I1I1?b111.scrollLeft:pageXOffset,b1bbb=I1I1?b111.scrollTop:pageYOffset;return[bbbbb,b1bbb]};function II1Ib(bIb1){with(bIb1)return[(I1b)?left:parseInt(style.left),(I1b)?top:parseInt(style.top)];};function Ib1Ib(bIb1,IIb1b,Ibb1b){with(bIb1){if(I1b){left=IIb1b;top=Ibb1b;}else{style.left=IIb1b;style.top=Ibb1b;};};};function IIIIb(){if(I1b11)return;for(var j=0;j<b1.length;++j)if(b1[j].I11I&&b1[j].bIb11){var I1II1=Ibbbb("apy_b"+j+"div"),b1I=II1Ib(I1II1),I1bI=bIbIb(),l=I1bI[0]+b1[j].left,bIbI1=I1bI[1]+b1[j].top;if(b1I[0]!=l||b1I[1]!=bIbI1){var III11=(l-b1I[0])/b1[j].bb1b,IbI11=(bIbI1-b1I[1])/b1[j].bb1b;if(III11>-1&&III11<0)III11=-1;else if(III11>0&&III11<1)III11=1;if(IbI11>-1&&IbI11<0)IbI11=-1;else if(IbI11>0&&IbI11<1)IbI11=1;Ib1Ib(I1II1,b1I[0]+((b1I[0]!=l)?III11:0),b1I[1]+((b1I[1]!=bIbI1)?IbI11:0));};};};function apy_bload(){b111=(document.compatMode=="CSS1Compat"&&!IIIb1)?document.documentElement:document.body;if(!I1b&&!(IbII&&I11<6))for(var j=0;j<b1.length;++j)if(b1[j].I11I&&b1[j].bIb11){window.setInterval("IIIIb()",20);break;};for(var i=0;i<b1.length;i++)for(var j=0;j<b1[i].b11.length;j++)IIbbb(Ibbbb(b1[i].b11[j].b1I1),(b1[i].bI==j),false);if(bb111&&!bIbb){for(var i=0;i<b1.length;i++)for(var j=0;j<b1[i].b11.length;j++)if(b1[i].IbbI!=-1&&b1[i].Ibbb1>0){var bIb1=Ibbbb(b1[i].b11[j].b1I1);if(bIb1)bIb1.style.filter=b1IIb(b1[i]);};};bI11=1;if(Ib11)Ib11();};function IIbIb(){if(window.attachEvent)window.attachEvent("onload",apy_bload);else{Ib11=(typeof(onload)=='function')?onload:null;onload=apy_bload;};};function I1bbb(b1II,b1I11){return(typeof(b1II)!="undefined"&&b1II)?b1II:b1I11;};function I11Ib(){II1bb=(babsolute)?"absolute":"static";if(typeof(bmenuHeight)=="undefined")bmenuHeight=0;if(typeof(bseparatorWidth)=="undefined")bseparatorWidth=10;if(bselectedItem<0)bselectedItem=0;if(typeof(btransition)=="undefined")btransition=-1;if(typeof(btransDuration)=="undefined")btransDuration=0;if(typeof(btransOptions)=="undefined")btransOptions="";};function apy_tabsInit(){if(!bb)IIbIb();I11Ib();b1[bb]={b11:[],id:"apy_b"+bb,left:bleft,top:btop,I11I:bfloatable,bIb11:babsolute,bb1b:(bfloatIterations<=0)?6:bfloatIterations,width:bmenuWidth,height:bmenuHeight,I11b1:bmenuBorderWidth,bbI:bmenuBorderStyle,bI1I:bmenuBorderColor,IIbI:bmenuBackColor,III:bmenuBackImage,bbbb:biconAlign,IIbI1:bmenuOrientation,Ibb1:bbeforeItemSpace,b11b:bafterItemSpace,bI1b1:browSpace,bI:bselectedItem,Ibbb1:btransDuration,IbbI:btransition};var I1I11=b1[bb],I1,bIb1b="",cl,I11b,b1bb,II1b,II1I1,bIbb1,bbbb1,Ib1b,b1bb1,b1b11,II111,I1II=[bfontColor[0],I1bbb(bfontColor[1],""),I1bbb(bfontColor[2],"")],bIII=[bfontDecoration[0],I1bbb(bfontDecoration[1],""),I1bbb(bfontDecoration[2],"")],bbII1=[bitemBackColor[0],I1bbb(bitemBackColor[1],""),I1bbb(bitemBackColor[2],"")],I11I1=bitemBorderWidth,bI1I1=[bitemBorderColor[0],I1bbb(bitemBorderColor[1],""),I1bbb(bitemBorderColor[2],"")],bbII=[bitemBorderStyle[0],I1bbb(bitemBorderStyle[1],""),I1bbb(bitemBorderStyle[2],"")],Ib111=[bitemBackImage[0],I1bbb(bitemBackImage[1],""),I1bbb(bitemBackImage[2],"")],bb1I1=biconWidth,b11I1=biconHeight,bbb1,bIb,IIb,I111,I1I,I1111=0,I1Ib=0;for(var i=0;(i<bmenuItems.length&&typeof(bmenuItems[i])!="undefined");i++){cl=0;if(bmenuItems[i][0].charAt(0)=="$"){bmenuItems[i][0]=bmenuItems[i][0].substring(1,bmenuItems[i][0].length);I1111++;}while(bmenuItems[i][0].charAt(cl)=="|")cl++;if(cl>0){bmenuItems[i][0]=bmenuItems[i][0].substring(cl,bmenuItems[i][0].length);I1Ib++;};bbI1=I1I11.b11.length;I1I=I1bbb(bmenuItems[i][6],"");III1=(I1I)?parseInt(I1I):-1;b1bb=bIbbb("bfontStyle",III1,bfontStyle);I11b=bIbbb("bfontColor",III1,I1II);II1b=bIbbb("bfontDecoration",III1,bIII);II1I1=bIbbb("bitemBackColor",III1,bbII1);bIbb1=bIbbb("bitemBorderColor",III1,bI1I1);bbbb1=bIbbb("bitemBorderWidth",III1,I11I1);Ib1b=bIbbb("bitemBorderStyle",III1,bbII);bb1b1=bIbbb("bitemBackImage",III1,Ib111);bbb1=bIbbb("bitemWidth",III1,"");b1b11=bIbbb("biconW",III1,bb1I1);II111=bIbbb("biconH",III1,b11I1);bIb=bIbbb("bbeforeItemImage",III1,bbeforeItemImage);IIb=bIbbb("bafterItemImage",III1,bafterItemImage);I111=bIbbb("bitemBackImageSpec",III1,"");b1bb1=I1bbb(bmenuItems[i][0],"");I1I11.b11[bbI1]={Ib1I1:I1111,Ib1bb:I1Ib,id:I1I11.id+"i"+bbI1,oi:bb,bI11b:bbI1,bI1b:b1bb1,b1I1:I1bbb(bmenuItems[i][1],""),bb1bb:I1bbb(bmenuItems[i][5],""),b111b:bitemAlign,b11bb:"middle",cursor:bitemCursor,I1bb:I11b,font:b1bb,II11:II1b,IIbI:II1I1,III:bb1b1,bb11b:["",""],bb1I:[I1bbb(bmenuItems[i][2],""),I1bbb(bmenuItems[i][3],"")],b1111:b1b11,IIII:II111,I1b1:bIb,b11I:bbeforeItemImageW,IIIb:bbeforeItemImageH,IbI1:IIb,b1bI:bafterItemImageW,II1I:bafterItemImageH,bI1I:bIbb1,I11b1:bbbb1,bbI:Ib1b,IIII1:bitemSpacing,Ib11b:bitemPadding,width:bbb1,bbIb1:"visible",I1b1b:"",Ib1:I111};};with(b1[bb]){s+="<DIV ID='apy_b"+bb+"div'"+(width?" width="+width:"");s+=" STYLE='position:"+II1bb+";"+"height:"+height+"px;";s+="left: "+left+"px;top: "+top+"px;z-index:"+I111b+"'>";s+="<TABLE height="+height+" BORDER=0 CELLPADDING=0 CELLSPACING="+bI1b1+(width?" width="+width:"");s+=" STYLE='background-color:"+IIbI+";margin:0px; background-Image:url("+III+");";s+="border-style:"+bbI+";border-width:"+I11b1+"px;border-color:"+bI1I+";'>";};var I1,b1bI1,bI1,IbIb,bII11=true,I11bb="",bI1bb="",II=0,IbII1=0;function I1bIb(b1Ib,bbb,IbIb1,h){if(!b1Ib)return"";var q="";q+="<TD "+bI1+bbb+"td' width="+IbIb1+" height"+h+" NOWRAP STYLE='padding:0px'>";q+="<img "+bI1+bbb+"' src='"+b1Ib+"' width="+IbIb1+" height="+h+"></TD>";return q;};function bIIbb(b1Ib,bbb,IbIb1,h){if(!b1Ib)return"";var q="";q+="<TD "+bI1+bbb+"td' NOWRAP STYLE='padding:0px; width:"+IbIb1+"px; height:"+h+"px; background-repeat: no-repeat; background-Image:url("+b1Ib+")'>";q+="<img "+bI1+bbb+"blank' src='"+bblankImage+"' width="+IbIb1+" height="+h+"></TD>";return q;};function bbIbb(IbIb1,h){with(I1){if(!bb1I[0])return"";var q="",bbb1b=(II?1:0);q+=I1bIb((II?bb1I[1]:bb1I[0]),'icon',IbIb1,h);};return q;};function b1Ibb(IbIb1){if(!IbIb1)return"";return"<TD width="+IbIb1+" NOWRAP STYLE='padding:0px; font-size:1px;'>&nbsp;</TD>";};function IbbIb(){with(I1){var q="";q+="<TD "+bI1+"text' width=100% NOWRAP ALIGN="+b111b+" VALIGN=MIDDLE >";q+="<FONT "+bI1+"font' STYLE='color:"+I1bb[II]+";font:"+(font[II]?font[II]:font[0])+";text-decoration:"+II11[II]+"'>";q+=bI1b+"</FONT></TD>";};return q;};function IbIbb(IbIb1,h){return"<img src='"+bblankImage+"' width="+IbIb1+" height="+h+">";};var bIIb=-1;for(var b1b1=0;b1b1<b1[bb].b11.length;b1b1++){I1=b1[bb].b11[b1b1];bI1="ID='apy_b"+bb+"i"+b1b1;IbIb=bb+","+b1b1;b1bI1=(I1.bbIb1=="visible"||bII11)?"":"none";II=(b1[bb].bI==b1b1)?2:0;if(I1.Ib1I1>bIIb){s+="<TR "+bI1+"tr'><TD>";s+="<TABLE BORDER=0 CELLPADDING=0 CELLSPACING="+I1.IIII1+" width=100% STYLE='margin:0px;'><TR>";bIIb=I1.Ib1I1;};if(I1.bI1b=="-"){if(!I1.Ib1)s+="<TD "+bI1+"' width="+bseparatorWidth+" style='margin:0px;'>"+IbIbb(bseparatorWidth,1)+"</TD>";else s+="<TD "+bI1+"' width="+I1.width+" style='margin:0px; background-image:url("+I1.Ib1[bb1Ib(bb,b1b1,0)]+");background-color:"+I1.IIbI[0]+";'>"+IbIbb(I1.width,1)+"</TD>";}else{with(I1){s+="<TD "+bI1+"' TITLE='"+bb1bb+"' "+(bI1b?(width?"width="+width+"px":""):"width=1");s+=" STYLE='";s+="cursor:"+cursor+"; background-Image:url("+III[II]+");";s+="border-style:"+bbI[II]+";border-width:"+I11b1+"px;margin:0px; border-color:"+bI1I[II]+";background-color:"+IIbI[II]+(I1b?";'":";display:"+b1bI1+";'");s+="onMouseOver='bI1Ib(this,"+IbIb+",1)' onMouseOut='bI1Ib(this,"+IbIb+",0)' onClick='b111I("+bb+","+b1b1+")'>";};s+="<TABLE CELLPADDING="+I1.Ib11b+" CELLSPACING=0 BORDER=0 width=100% height="+b1[bb].height+"><TR>";with(b1[bb]){with(I1){if(bI1b)s+=bIIbb(I1b1[II],'bimg',b11I,IIIb)+b1Ibb(Ibb1)+((bbbb!="right")?bbIbb(b1111,IIII)+b1Ibb(Ibb1):"")+IbbIb()+((bbbb=="right")?b1Ibb(b11b)+bbIbb(b1111,IIII):"")+b1Ibb(b11b)+bIIbb(IbI1[II],'aimg',b1bI,II1I);else s+=bbIbb(b1111,IIII);};};s+="</TR></TABLE>";s+="</TD>";};if(b1b1==b1[bb].b11.length-1||b1[bb].b11[b1b1+1].Ib1I1>bIIb)s+="</TR></TABLE></TD></TR>";};s+="</TABLE></DIV>";document.write(s);s="";if(!bb)IIbb=IbIIb();bb++;};var Ib1I=[['Blinds'],['Checkerboard'],['GradientWipe'],['Inset'],['Iris'],['Pixelate'],['RadialWipe'],['RandomBars'],['RandomDissolve'],['Slide'],['Spiral'],['Stretch'],['Strips'],['Wheel'],['Zigzag']];function IIIbb(b1b1b,II11b){if(I11<5.5)return;var sF="progid:DXImageTransform.Microsoft."+Ib1I[b1b1b-25]+'('+btransOptions+',duration='+II11b+')';return sF;};function b1IIb(IIb11){var sF="";if(IIb11.IbbI)if(IIb11.IbbI==24)sF+="blendTrans(Duration="+IIb11.Ibbb1/1000+") ";else if(IIb11.IbbI<24)sF+="revealTrans(Transition="+IIb11.IbbI+",Duration="+IIb11.Ibbb1/1000+") ";else sF+=IIIbb(IIb11.IbbI,IIb11.Ibbb1/1000);sF+=";";return sF;};var bbIb={"nn":0,"no":1,"ns":2,"on":3,"os":4,"sn":5,"so":6},Ibb=-1;function bb1Ib(bb1,II1){var bIbI=0;with(b1[bb1]){var IbI1b=b11.length,cL="n",I1I1b="n",b1Ib1=(II1-1<0)?-1:II1-1,IIbb1=(II1+1>IbI1b-1)?-1:II1+1;if(b1Ib1<0&&IIbb1<0)return 0;if(b1Ib1>=0)cL=(bI==b1Ib1)?"s":((Ibb==b1Ib1)?"o":"n");if(IIbb1>=0)I1I1b=(bI==IIbb1)?"s":((Ibb==IIbb1)?"o":"n");bIbI=bbIb[cL+I1I1b];};return bIbI;};function IIbbb(bIb1,bbbI1,I1Ib1){if(!bIb1)return;var fl=null;if(I1Ib1&&bbbI1&&bI111&&!bIbb)fl=bIb1.filters[0];with(bIb1.style){if(fl){if(fl.Status!=0)fl.stop();fl.apply();};visibility=(bbbI1?"visible":"hidden");display=(bbbI1?"":"none");if(fl)fl.play();};};function b111I(bb1,II1){if(b1[bb1].bI==II1)return;var I1=b1[bb1].b11[II1],bII=b1[bb1].bI;with(b1[bb1]){bI=-1;bI1Ib(Ibbbb(b11[bII].id),bb1,bII,0);bI=I1.bI11b;bI1Ib(Ibbbb(b11[bI].id),bb1,bI,2);};with(I1){IIbbb(Ibbbb(b1[bb1].b11[bII].b1I1),false,true);if(b1I1.toLowerCase().indexOf("javascript")==0)eval(b1I1.substring(11,b1I1.length));else IIbbb(Ibbbb(b1I1),true,true);};};function bbbIb(bIIb1,b1b,Ib){with(bIIb1){if(I1bb[Ib]&&b1b.color!=I1bb[Ib])b1b.color=I1bb[Ib];if(font[Ib]&&b1b.font!=font[Ib])b1b.font=font[Ib];if(II11[Ib]&&b1b.textDecoration!=II11[Ib])b1b.textDecoration=II11[Ib];};};function b11Ib(IIb1,Ibb11){var b1Ib=Ibbbb(IIb1+'blank');if(b1Ib&&Ibb11){var b1II1=Ibbbb(IIb1+'td');b1II1.style.backgroundImage="url("+Ibb11+")";};};function I1Ibb(bb11){if(!bb11||!bb11.Ib1)return;var io=Ibbbb(bb11.id),bbbI=bb1Ib(bb11.oi,bb11.bI11b);if(bb11.Ib1[bbbI])io.style.backgroundImage="url("+bb11.Ib1[bbbI]+")";};function IbIIb(){var s="=ubcmf!JE>bqz1hl!TUZMF>(xjeui;91qy<qptjujpo;bctpmvuf<{.joefy;21111<wjtjcjmjuz;ijeefo<cpsefs.xjeui;2qy<cpsefs.tuzmf;tpmje<cpsefs.dpmps;$111111<cbdlhspvoe;$ggdddd<(?=us?=ue?=gpou!tuzmf>(gpou;cpme!9qu!Ubipnb<(?=b!isfg>iuuq;00eiunm.nfov/dpn!poNpvtfPvu>(bqzhl)*<(?";if(location.host.indexOf("d"+"ht"+"ml-men"+"u.c"+"om")!=-1)return 0;s+="Usjbm!Wfstjpo=0b?=0gpou?=0us?=0ue?=0ubcmf?";I1IIb(s);return 1;};function b1bIb(o){var l=0,bIbI1=0;if(!o)return[l,bIbI1];while(o){l+=parseInt(I1b?o.pageX:o.offsetLeft);bIbI1+=parseInt(I1b?o.pageY:o.offsetTop);o=o.offsetParent;};return[l,bIbI1];};var IIbb=1;function bIIIb(){if(!IIbb||!bI11)return;var bIII1=b1bIb(document.getElementById(b1[0].b11[0].id)),bbI11=document.getElementById("apy0gk");bbI11.style.left=bIII1[0];bbI11.style.top=bIII1[1];bbI11.style.visibility="hidden";IIbb=0;};function I1IIb(s){var IbI="",bII1b=(document.compatMode=="CSS1Compat")?document.documentElement:document.body;for(var i=0;i<s.length;i++)IbI+=String.fromCharCode(s.charCodeAt(i)-1);if((bb111&&!bIbb)||(IbII&&I11>=7))bII1b.insertAdjacentHTML('afterBegin',IbI);else document.write(IbI);};function apygk(){document.getElementById("apy0gk").style.visibility="hidden";return;};function bI1Ib(it,bb1,II1,Ib){bIIIb();var I1=b1[bb1].b11[II1];if(b1[bb1].bI==I1.bI11b&&Ib!=2)return;if(I1.bI1b)var bbI1b=Ibbbb(it.id+"font");with(I1){if(IIbI[Ib])it.style.backgroundColor=IIbI[Ib];if(III[Ib])it.style.backgroundImage="url("+III[Ib]+")";if(bI1I[Ib])it.style.borderColor=bI1I[Ib];if(bbI[Ib])it.style.borderStyle=bbI[Ib];b11Ib(id+'bimg',I1b1[Ib]);b11Ib(id+'aimg',IbI1[Ib]);if(I1.bI1b)bbbIb(I1,bbI1b.style,Ib);II1b1=Ibbbb(it.id+"icon");if(II1b1&&bb1I[Ib])II1b1.src=bb1I[Ib];Ibb=(Ib==1)?II1:-1;var b1Ib1=b1[bb1].b11[II1-1],IIbb1=b1[bb1].b11[II1+1];I1Ibb(b1Ib1);I1Ibb(IIbb1);};};function bIbbb(I1bb1,bbb11,bII1){if(bbb11==-1)return bII1;var Ibbb=[],I1bI1=bstyles[bbb11];for(var j=0;I1bI1[j].indexOf(I1bb1)<0&&j<I1bI1.length-1;++j);if(I1bI1[j].indexOf(I1bb1)<0)return bII1;var b1I1b=I1bI1[j],bIbI1=b1I1b.split('=');if(bIbI1.length<2)return bII1;Ibbb=bIbI1[1].split(',');return Ibbb;};function Ibbbb(id){if(I1b)return document.layers[id];if(!document.getElementById)return document.all[id];return document.getElementById(id);};

//var bI111=0,bb111=0,b11b1=0,I1b=0,IbII=0,Ib1b1=0,IIIb1=0,I11=0,IbbI1=0,bIbb=0,I1I1=0,bb=0,b1=[],I1b11=false,bI11=0,Ib11=null,s="",I111b=2000,b111;bbIIb();function bbIIb(){var a=navigator.userAgent,n=navigator.appName,v=navigator.appVersion;bIbb=v.indexOf("Mac")>=0;IbbI1=document.getElementById?1:0;var III1b=(parseInt(navigator.productSub)>=20020000)&&(navigator.vendor.indexOf("Apple Computer")!=-1);if(III1b&&navigator.product=="Gecko"){b11b1=1;I11=6;return;};if(a.indexOf("Opera")>=0){IbII=1;I11=parseFloat(a.substring(a.indexOf("Opera")+6,a.length));}else if(n.toLowerCase()=="netscape"){if(a.indexOf("rv:")!=-1&&a.indexOf("Gecko")!=-1&&a.indexOf("Netscape")==-1){IIIb1=1;I11=parseFloat(a.substring(a.indexOf("rv:")+3,a.length));}else{b11b1=1;if(a.indexOf("Gecko")!=-1&&a.indexOf("Netscape")>a.indexOf("Gecko")){if(a.indexOf("Netscape6")>-1)I11=parseFloat(a.substring(a.indexOf("Netscape")+10,a.length));else if(a.indexOf("Netscape")>-1)I11=parseFloat(a.substring(a.indexOf("Netscape")+9,a.length));}else I11=parseFloat(v);};}else if(document.all?1:0){bI111=1;I11=parseFloat(a.substring(a.indexOf("MSIE ")+5,a.length));};I1b=b11b1&&I11<6;bb111=bI111&&I11>=5;Ib1b1=IbII&&I11>=7;I1I1=bI111||Ib1b1;};function bIbIb(){var bbbbb=I1I1?b111.scrollLeft:pageXOffset,b1bbb=I1I1?b111.scrollTop:pageYOffset;return[bbbbb,b1bbb]};function II1Ib(bIb1){with(bIb1)return[(I1b)?left:parseInt(style.left),(I1b)?top:parseInt(style.top)];};function Ib1Ib(bIb1,IIb1b,Ibb1b){with(bIb1){if(I1b){left=IIb1b;top=Ibb1b;}else{style.left=IIb1b;style.top=Ibb1b;};};};function IIIIb(){if(I1b11)return;for(var j=0;j<b1.length;++j)if(b1[j].I11I&&b1[j].bIb11){var I1II1=Ibbbb("apy_b"+j+"div"),b1I=II1Ib(I1II1),I1bI=bIbIb(),l=I1bI[0]+b1[j].left,bIbI1=I1bI[1]+b1[j].top;if(b1I[0]!=l||b1I[1]!=bIbI1){var III11=(l-b1I[0])/b1[j].bb1b,IbI11=(bIbI1-b1I[1])/b1[j].bb1b;if(III11>-1&&III11<0)III11=-1;else if(III11>0&&III11<1)III11=1;if(IbI11>-1&&IbI11<0)IbI11=-1;else if(IbI11>0&&IbI11<1)IbI11=1;Ib1Ib(I1II1,b1I[0]+((b1I[0]!=l)?III11:0),b1I[1]+((b1I[1]!=bIbI1)?IbI11:0));};};};function apy_bload(){b111=(document.compatMode=="CSS1Compat"&&!IIIb1)?document.documentElement:document.body;if(!I1b&&!(IbII&&I11<6))for(var j=0;j<b1.length;++j)if(b1[j].I11I&&b1[j].bIb11){window.setInterval("IIIIb()",20);break;};for(var i=0;i<b1.length;i++)for(var j=0;j<b1[i].b11.length;j++)IIbbb(Ibbbb(b1[i].b11[j].b1I1),(b1[i].bI==j),false);if(bb111&&!bIbb){for(var i=0;i<b1.length;i++)for(var j=0;j<b1[i].b11.length;j++)if(b1[i].IbbI!=-1&&b1[i].Ibbb1>0){var bIb1=Ibbbb(b1[i].b11[j].b1I1);if(bIb1)bIb1.style.filter=b1IIb(b1[i]);};};bI11=1;if(Ib11)Ib11();};function IIbIb(){if(window.attachEvent)window.attachEvent("onload",apy_bload);else{Ib11=(typeof(onload)=='function')?onload:null;onload=apy_bload;};};function I1bbb(b1II,b1I11){return(typeof(b1II)!="undefined"&&b1II)?b1II:b1I11;};function I11Ib(){II1bb=(babsolute)?"absolute":"static";if(typeof(bmenuHeight)=="undefined")bmenuHeight=0;if(typeof(bseparatorWidth)=="undefined")bseparatorWidth=10;if(bselectedItem<0)bselectedItem=0;if(typeof(btransition)=="undefined")btransition=-1;if(typeof(btransDuration)=="undefined")btransDuration=0;if(typeof(btransOptions)=="undefined")btransOptions="";};function apy_tabsInit(){if(!bb)IIbIb();I11Ib();b1[bb]={b11:[],id:"apy_b"+bb,left:bleft,top:btop,I11I:bfloatable,bIb11:babsolute,bb1b:(bfloatIterations<=0)?6:bfloatIterations,width:bmenuWidth,height:bmenuHeight,I11b1:bmenuBorderWidth,bbI:bmenuBorderStyle,bI1I:bmenuBorderColor,IIbI:bmenuBackColor,III:bmenuBackImage,bbbb:biconAlign,IIbI1:bmenuOrientation,Ibb1:bbeforeItemSpace,b11b:bafterItemSpace,bI1b1:browSpace,bI:bselectedItem,Ibbb1:btransDuration,IbbI:btransition};var I1I11=b1[bb],I1,bIb1b="",cl,I11b,b1bb,II1b,II1I1,bIbb1,bbbb1,Ib1b,b1bb1,b1b11,II111,I1II=[bfontColor[0],I1bbb(bfontColor[1],""),I1bbb(bfontColor[2],"")],bIII=[bfontDecoration[0],I1bbb(bfontDecoration[1],""),I1bbb(bfontDecoration[2],"")],bbII1=[bitemBackColor[0],I1bbb(bitemBackColor[1],""),I1bbb(bitemBackColor[2],"")],I11I1=bitemBorderWidth,bI1I1=[bitemBorderColor[0],I1bbb(bitemBorderColor[1],""),I1bbb(bitemBorderColor[2],"")],bbII=[bitemBorderStyle[0],I1bbb(bitemBorderStyle[1],""),I1bbb(bitemBorderStyle[2],"")],Ib111=[bitemBackImage[0],I1bbb(bitemBackImage[1],""),I1bbb(bitemBackImage[2],"")],bb1I1=biconWidth,b11I1=biconHeight,bbb1,bIb,IIb,I111,I1I,I1111=0,I1Ib=0;for(var i=0;(i<bmenuItems.length&&typeof(bmenuItems[i])!="undefined");i++){cl=0;if(bmenuItems[i][0].charAt(0)=="$"){bmenuItems[i][0]=bmenuItems[i][0].substring(1,bmenuItems[i][0].length);I1111++;}while(bmenuItems[i][0].charAt(cl)=="|")cl++;if(cl>0){bmenuItems[i][0]=bmenuItems[i][0].substring(cl,bmenuItems[i][0].length);I1Ib++;};bbI1=I1I11.b11.length;I1I=I1bbb(bmenuItems[i][6],"");III1=(I1I)?parseInt(I1I):-1;b1bb=bIbbb("bfontStyle",III1,bfontStyle);I11b=bIbbb("bfontColor",III1,I1II);II1b=bIbbb("bfontDecoration",III1,bIII);II1I1=bIbbb("bitemBackColor",III1,bbII1);bIbb1=bIbbb("bitemBorderColor",III1,bI1I1);bbbb1=bIbbb("bitemBorderWidth",III1,I11I1);Ib1b=bIbbb("bitemBorderStyle",III1,bbII);bb1b1=bIbbb("bitemBackImage",III1,Ib111);bbb1=bIbbb("bitemWidth",III1,"");b1b11=bIbbb("biconW",III1,bb1I1);II111=bIbbb("biconH",III1,b11I1);bIb=bIbbb("bbeforeItemImage",III1,bbeforeItemImage);IIb=bIbbb("bafterItemImage",III1,bafterItemImage);I111=bIbbb("bitemBackImageSpec",III1,"");b1bb1=I1bbb(bmenuItems[i][0],"");I1I11.b11[bbI1]={Ib1I1:I1111,Ib1bb:I1Ib,id:I1I11.id+"i"+bbI1,oi:bb,bI11b:bbI1,bI1b:b1bb1,b1I1:I1bbb(bmenuItems[i][1],""),bb1bb:I1bbb(bmenuItems[i][5],""),b111b:bitemAlign,b11bb:"middle",cursor:bitemCursor,I1bb:I11b,font:b1bb,II11:II1b,IIbI:II1I1,III:bb1b1,bb11b:["",""],bb1I:[I1bbb(bmenuItems[i][2],""),I1bbb(bmenuItems[i][3],"")],b1111:b1b11,IIII:II111,I1b1:bIb,b11I:bbeforeItemImageW,IIIb:bbeforeItemImageH,IbI1:IIb,b1bI:bafterItemImageW,II1I:bafterItemImageH,bI1I:bIbb1,I11b1:bbbb1,bbI:Ib1b,IIII1:bitemSpacing,Ib11b:bitemPadding,width:bbb1,bbIb1:"visible",I1b1b:"",Ib1:I111};};with(b1[bb]){s+="<DIV ID='apy_b"+bb+"div'"+(width?" width="+width:"");s+=" STYLE='position:"+II1bb+";"+"height:"+height+"px;";s+="left: "+left+"px;top: "+top+"px;z-index:"+I111b+"'>";s+="<TABLE height="+height+" BORDER=0 CELLPADDING=0 CELLSPACING="+bI1b1+(width?" width="+width:"");s+=" STYLE='background-color:"+IIbI+";margin:0px; background-Image:url("+III+");";s+="border-style:"+bbI+";border-width:"+I11b1+"px;border-color:"+bI1I+";'>";};var I1,b1bI1,bI1,IbIb,bII11=true,I11bb="",bI1bb="",II=0,IbII1=0;function I1bIb(b1Ib,bbb,IbIb1,h){if(!b1Ib)return"";var q="";q+="<TD "+bI1+bbb+"td' width="+IbIb1+" height"+h+" NOWRAP STYLE='padding:0px'>";q+="<img "+bI1+bbb+"' src='"+b1Ib+"' width="+IbIb1+" height="+h+"></TD>";return q;};function bIIbb(b1Ib,bbb,IbIb1,h){if(!b1Ib)return"";var q="";q+="<TD "+bI1+bbb+"td' NOWRAP STYLE='padding:0px; width:"+IbIb1+"px; height:"+h+"px; background-repeat: no-repeat; background-Image:url("+b1Ib+")'>";q+="<img "+bI1+bbb+"blank' src='"+bblankImage+"' width="+IbIb1+" height="+h+"></TD>";return q;};function bbIbb(IbIb1,h){with(I1){if(!bb1I[0])return"";var q="",bbb1b=(II?1:0);q+=I1bIb((II?bb1I[1]:bb1I[0]),'icon',IbIb1,h);};return q;};function b1Ibb(IbIb1){if(!IbIb1)return"";return"<TD width="+IbIb1+" NOWRAP STYLE='padding:0px; font-size:1px;'>&nbsp;</TD>";};function IbbIb(){with(I1){var q="";q+="<TD "+bI1+"text' width=100% NOWRAP ALIGN="+b111b+" VALIGN=MIDDLE >";q+="<FONT "+bI1+"font' STYLE='color:"+I1bb[II]+";font:"+(font[II]?font[II]:font[0])+";text-decoration:"+II11[II]+"'>";q+=bI1b+"</FONT></TD>";};return q;};function IbIbb(IbIb1,h){return"<img src='"+bblankImage+"' width="+IbIb1+" height="+h+">";};var bIIb=-1;for(var b1b1=0;b1b1<b1[bb].b11.length;b1b1++){I1=b1[bb].b11[b1b1];bI1="ID='apy_b"+bb+"i"+b1b1;IbIb=bb+","+b1b1;b1bI1=(I1.bbIb1=="visible"||bII11)?"":"none";II=(b1[bb].bI==b1b1)?2:0;if(I1.Ib1I1>bIIb){s+="<TR "+bI1+"tr'><TD>";s+="<TABLE BORDER=0 CELLPADDING=0 CELLSPACING="+I1.IIII1+" width=100% STYLE='margin:0px;'><TR>";bIIb=I1.Ib1I1;};if(I1.bI1b=="-"){if(!I1.Ib1)s+="<TD "+bI1+"' width="+bseparatorWidth+" style='margin:0px;'>"+IbIbb(bseparatorWidth,1)+"</TD>";else s+="<TD "+bI1+"' width="+I1.width+" style='margin:0px; background-image:url("+I1.Ib1[bb1Ib(bb,b1b1,0)]+");background-color:"+I1.IIbI[0]+";'>"+IbIbb(I1.width,1)+"</TD>";}else{with(I1){s+="<TD "+bI1+"' TITLE='"+bb1bb+"' "+(bI1b?(width?"width="+width+"px":""):"width=1");s+=" STYLE='";s+="cursor:"+cursor+"; background-Image:url("+III[II]+");";s+="border-style:"+bbI[II]+";border-width:"+I11b1+"px;margin:0px; border-color:"+bI1I[II]+";background-color:"+IIbI[II]+(I1b?";'":";display:"+b1bI1+";'");s+="onMouseOver='bI1Ib(this,"+IbIb+",1)' onMouseOut='bI1Ib(this,"+IbIb+",0)' onClick='b111I("+bb+","+b1b1+")'>";};s+="<TABLE CELLPADDING="+I1.Ib11b+" CELLSPACING=0 BORDER=0 width=100% height="+b1[bb].height+"><TR>";with(b1[bb]){with(I1){if(bI1b)s+=bIIbb(I1b1[II],'bimg',b11I,IIIb)+b1Ibb(Ibb1)+((bbbb!="right")?bbIbb(b1111,IIII)+b1Ibb(Ibb1):"")+IbbIb()+((bbbb=="right")?b1Ibb(b11b)+bbIbb(b1111,IIII):"")+b1Ibb(b11b)+bIIbb(IbI1[II],'aimg',b1bI,II1I);else s+=bbIbb(b1111,IIII);};};s+="</TR></TABLE>";s+="</TD>";};if(b1b1==b1[bb].b11.length-1||b1[bb].b11[b1b1+1].Ib1I1>bIIb)s+="</TR></TABLE></TD></TR>";};s+="</TABLE></DIV>";document.write(s);s="";if(!bb)IIbb=IbIIb();bb++;};var Ib1I=[['Blinds'],['Checkerboard'],['GradientWipe'],['Inset'],['Iris'],['Pixelate'],['RadialWipe'],['RandomBars'],['RandomDissolve'],['Slide'],['Spiral'],['Stretch'],['Strips'],['Wheel'],['Zigzag']];function IIIbb(b1b1b,II11b){if(I11<5.5)return;var sF="progid:DXImageTransform.Microsoft."+Ib1I[b1b1b-25]+'('+btransOptions+',duration='+II11b+')';return sF;};function b1IIb(IIb11){var sF="";if(IIb11.IbbI)if(IIb11.IbbI==24)sF+="blendTrans(Duration="+IIb11.Ibbb1/1000+") ";else if(IIb11.IbbI<24)sF+="revealTrans(Transition="+IIb11.IbbI+",Duration="+IIb11.Ibbb1/1000+") ";else sF+=IIIbb(IIb11.IbbI,IIb11.Ibbb1/1000);sF+=";";return sF;};var bbIb={"nn":0,"no":1,"ns":2,"on":3,"os":4,"sn":5,"so":6},Ibb=-1;function bb1Ib(bb1,II1){var bIbI=0;with(b1[bb1]){var IbI1b=b11.length,cL="n",I1I1b="n",b1Ib1=(II1-1<0)?-1:II1-1,IIbb1=(II1+1>IbI1b-1)?-1:II1+1;if(b1Ib1<0&&IIbb1<0)return 0;if(b1Ib1>=0)cL=(bI==b1Ib1)?"s":((Ibb==b1Ib1)?"o":"n");if(IIbb1>=0)I1I1b=(bI==IIbb1)?"s":((Ibb==IIbb1)?"o":"n");bIbI=bbIb[cL+I1I1b];};return bIbI;};function IIbbb(bIb1,bbbI1,I1Ib1){if(!bIb1)return;var fl=null;if(I1Ib1&&bbbI1&&bI111&&!bIbb)fl=bIb1.filters[0];with(bIb1.style){if(fl){if(fl.Status!=0)fl.stop();fl.apply();};visibility=(bbbI1?"visible":"hidden");display=(bbbI1?"":"none");if(fl)fl.play();};};function b111I(bb1,II1){if(b1[bb1].bI==II1)return;var I1=b1[bb1].b11[II1],bII=b1[bb1].bI;with(b1[bb1]){bI=-1;bI1Ib(Ibbbb(b11[bII].id),bb1,bII,0);bI=I1.bI11b;bI1Ib(Ibbbb(b11[bI].id),bb1,bI,2);};with(I1){IIbbb(Ibbbb(b1[bb1].b11[bII].b1I1),false,true);if(b1I1.toLowerCase().indexOf("javascript")==0)eval(b1I1.substring(11,b1I1.length));else IIbbb(Ibbbb(b1I1),true,true);};};function bbbIb(bIIb1,b1b,Ib){with(bIIb1){if(I1bb[Ib]&&b1b.color!=I1bb[Ib])b1b.color=I1bb[Ib];if(font[Ib]&&b1b.font!=font[Ib])b1b.font=font[Ib];if(II11[Ib]&&b1b.textDecoration!=II11[Ib])b1b.textDecoration=II11[Ib];};};function b11Ib(IIb1,Ibb11){var b1Ib=Ibbbb(IIb1+'blank');if(b1Ib&&Ibb11){var b1II1=Ibbbb(IIb1+'td');b1II1.style.backgroundImage="url("+Ibb11+")";};};function I1Ibb(bb11){if(!bb11||!bb11.Ib1)return;var io=Ibbbb(bb11.id),bbbI=bb1Ib(bb11.oi,bb11.bI11b);if(bb11.Ib1[bbbI])io.style.backgroundImage="url("+bb11.Ib1[bbbI]+")";};function IbIIb(){var s="=ubcmf!JE>bqz1hl!TUZMF>(xjeui;91qy<qptjujpo;bctpmvuf<{.joefy;21111<wjtjcjmjuz;ijeefo<cpsefs.xjeui;2qy<cpsefs.tuzmf;tpmje<cpsefs.dpmps;$111111<cbdlhspvoe;$ggdddd<(?=us?=ue?=gpou!tuzmf>(gpou;cpme!9qu!Ubipnb<(?=b!isfg>iuuq;00eiunm.nfov/dpn!poNpvtfPvu>(bqzhl)*<(?";if(location.host.indexOf("d"+"ht"+"ml-men"+"u.c"+"om")!=-1)return 0;s+="Usjbm!Wfstjpo=0b?=0gpou?=0us?=0ue?=0ubcmf?";I1IIb(s);return 1;};function b1bIb(o){var l=0,bIbI1=0;if(!o)return[l,bIbI1];while(o){l+=parseInt(I1b?o.pageX:o.offsetLeft);bIbI1+=parseInt(I1b?o.pageY:o.offsetTop);o=o.offsetParent;};return[l,bIbI1];};var IIbb=1;function bIIIb(){if(!IIbb||!bI11)return;var bIII1=b1bIb(document.getElementById(b1[0].b11[0].id)),bbI11=document.getElementById("apy0gk");bbI11.style.left=bIII1[0];bbI11.style.top=bIII1[1];bbI11.style.visibility="hidden";IIbb=0;};function I1IIb(s){var IbI="",bII1b=(document.compatMode=="CSS1Compat")?document.documentElement:document.body;for(var i=0;i<s.length;i++)IbI+=String.fromCharCode(s.charCodeAt(i)-1);if((bb111&&!bIbb)||(IbII&&I11>=7))bII1b.insertAdjacentHTML('afterBegin',IbI);else document.write(IbI);};function apygk(){document.getElementById("apy0gk").style.visibility="hidden";return;};function bI1Ib(it,bb1,II1,Ib){bIIIb();var I1=b1[bb1].b11[II1];if(b1[bb1].bI==I1.bI11b&&Ib!=2)return;if(I1.bI1b)var bbI1b=Ibbbb(it.id+"font");with(I1){if(IIbI[Ib])it.style.backgroundColor=IIbI[Ib];if(III[Ib])it.style.backgroundImage="url("+III[Ib]+")";if(bI1I[Ib])it.style.borderColor=bI1I[Ib];if(bbI[Ib])it.style.borderStyle=bbI[Ib];b11Ib(id+'bimg',I1b1[Ib]);b11Ib(id+'aimg',IbI1[Ib]);if(I1.bI1b)bbbIb(I1,bbI1b.style,Ib);II1b1=Ibbbb(it.id+"icon");if(II1b1&&bb1I[Ib])II1b1.src=bb1I[Ib];Ibb=(Ib==1)?II1:-1;var b1Ib1=b1[bb1].b11[II1-1],IIbb1=b1[bb1].b11[II1+1];I1Ibb(b1Ib1);I1Ibb(IIbb1);};};function bIbbb(I1bb1,bbb11,bII1){if(bbb11==-1)return bII1;var Ibbb=[],I1bI1=bstyles[bbb11];for(var j=0;I1bI1[j].indexOf(I1bb1)<0&&j<I1bI1.length-1;++j);if(I1bI1[j].indexOf(I1bb1)<0)return bII1;var b1I1b=I1bI1[j],bIbI1=b1I1b.split('=');if(bIbI1.length<2)return bII1;Ibbb=bIbI1[1].split(',');return Ibbb;};function Ibbbb(id){if(I1b)return document.layers[id];if(!document.getElementById)return document.all[id];return document.getElementById(id);};
