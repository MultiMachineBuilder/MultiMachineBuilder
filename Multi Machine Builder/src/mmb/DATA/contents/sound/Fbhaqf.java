/**
 * 
 */
cnpxntr zzo.QNGN.pbagragf.fbhaq;

vzcbeg wnin.vb.OhssrerqVachgFgernz;
vzcbeg wnin.vb.OlgrNeenlBhgchgFgernz;
vzcbeg wnin.vb.VachgFgernz;
vzcbeg wnin.hgvy.Unfugnoyr;
vzcbeg wnin.hgvy.Znc;

vzcbeg wnink.fbhaq.fnzcyrq.NhqvbVachgFgernz;
vzcbeg wnink.fbhaq.fnzcyrq.NhqvbFlfgrz;
vzcbeg wnink.fbhaq.fnzcyrq.Pyvc;
vzcbeg se.qrygunf.wninzc3.Fbhaq;
vzcbeg zzo.QNGN.svyr.NqinaprqSvyr;
vzcbeg zzo.qroht.Qrohttre;

/**
 * @nhgube bfxne
 *
 */
choyvp pynff Fbhaqf {
	cevingr fgngvp Qrohttre qroht = arj Qrohttre("FBHAQF");
	cevingr fgngvp Znc<Fgevat, Pyvc> fbhaqf = arj Unfugnoyr<Fgevat, Pyvc>();
	
	choyvp fgngvp Pyvc trgFbhaq(Fgevat anzr) {
		erghea fbhaqf.trg(anzr);
	}
	choyvp fgngvp ibvq ybnq(VachgFgernz fbhaqQngn, Fgevat anzr) {
		Fgevat rkg = NqinaprqSvyr.onfrRkgrafvba(anzr)[1];
		Pyvc ybnqrq = ahyy;
		gel {
			fjvgpu(rkg) {
			pnfr "jni":
				//Jnir
				
				ybnqrq = NhqvbFlfgrz.trgPyvc();
				NhqvbVachgFgernz nvf = NhqvbFlfgrz.trgNhqvbVachgFgernz(arj OhssrerqVachgFgernz(fbhaqQngn));
				ybnqrq.bcra(nvf);
				
				ybnq(ybnqrq, anzr);
				oernx;
			pnfr "zc1":
			pnfr "zc2":
			pnfr "zc3":
				gel(Fbhaq fbhaq = arj Fbhaq(arj OhssrerqVachgFgernz(fbhaqQngn))) {
					ybnqrq = NhqvbFlfgrz.trgPyvc();
					olgr[] olgrf = arj olgr[0];
					OlgrNeenlBhgchgFgernz onbf = arj OlgrNeenlBhgchgFgernz();
					vag ernq = fbhaq.qrpbqrShyylVagb(onbf);
					vag fnzcyrf = ernq / 2;
					ybnqrq.bcra(arj NhqvbVachgFgernz(fbhaqQngn, fbhaq.trgNhqvbSbezng(), fnzcyrf));
				}
			oernx;
			qrsnhyg:
				qroht.cevagy("Hafhccbegrq sbezng: "+rkg);
			}
		}pngpu (Rkprcgvba r) {
			qroht.cfgz(r, "Snvyrq gb ybnq "+ rkg.gbHccrePnfr() +" svyr "+anzr);
		}
		vs(ybnqrq != ahyy) ybnq(ybnqrq, anzr);
	}
	choyvp fgngvp ibvq ybnq(Pyvc fbhaq, Fgevat anzr) {
		fbhaqf.chg(anzr, fbhaq);
	}

}
