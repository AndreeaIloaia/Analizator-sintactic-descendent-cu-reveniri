import java.lang.reflect.Field;
import java.util.List;

public class AnalizatorDescReveniri2 {
    private String stare;
    private int pozitie;
    private String stivaLucru;
    private String bandaIntrare;
    private String bandaIntrareInitial;
    private Gramatica gramatica;
    private List<FIP> fip;

    AnalizatorDescReveniri2(String stare, int pozitie, String stivaLucru, String bandaIntrare, Gramatica gramatica, List<FIP> fip) {
        this.stare = stare;
        this.pozitie = pozitie;
        this.stivaLucru = stivaLucru;
        this.bandaIntrare = bandaIntrare;
        this.bandaIntrareInitial = bandaIntrare;
        this.gramatica = gramatica;
        this.fip = fip;
    }

    private int getElem(String seq) {
        int i = -1;
        if (Character.isUpperCase(seq.charAt(0))) {
            for (i = 0; i < seq.length(); i++) {
                if (!Character.isUpperCase(seq.charAt(0))) {
                    break;
                }
            }
        } else if (!Character.isLowerCase(seq.charAt(0))) {
            for (i = 0; i < seq.length(); i++) {
                if (!Character.isLowerCase(seq.charAt(0))) {
                    break;
                }
            }
        }
        return i;
    }

    private int getPozitieNeterminal(String neterminal) {
        int poz = -1;
        for (RP rp : gramatica.getRp()) {
            poz++;
            if (rp.getMembruStang().equals(neterminal)) {
                return poz;
            }
        }
        return poz;
    }

    // (q, i, alfa, Abeta) |- (q, i, alfaA(1), gama(i)beta)
    void expand() {
        String neterminal = bandaIntrare.split(" ")[0];
        RP rp = gramatica.getRp().get(getPozitieNeterminal(neterminal));
        if (stivaLucru.equals("eps")) {
            stivaLucru = "P" + rp.getNumber();
        } else {
            stivaLucru = stivaLucru + " P" + rp.getNumber();
        }
        removeSpaces();
        bandaIntrare = rp.getMembruDrept() + " " + bandaIntrare.substring(neterminal.length());
        removeSpaces();
    }

    // (q, i, alfa, a(i)beta) |- (q, i + 1, alfaa(i), beta)
    void avans() {
        pozitie++;
        removeSpaces();
        String terminal = bandaIntrare.split(" ")[0];
        stivaLucru = stivaLucru + " " + terminal;
        bandaIntrare = bandaIntrare.substring(terminal.length());
        if (bandaIntrare.isEmpty()) {
            bandaIntrare = "eps";
        }
        removeSpaces();
    }


    // (q, i, alfa, abeta) |- (r, i, alfa, abeta)
    void insuccesMoment() {
        stare = "r";
    }

    void altaIncercare(int caz) {
        //nr regulii de productie
        List<RP> reguliProductie = gramatica.getRp();
        removeSpaces();
        if (caz == 1) {
            stivaLucru = stivaLucru.trim();
            int poz = stivaLucru.lastIndexOf('P');

            if (stivaLucru.charAt(stivaLucru.length() - 2) != 'P' && stivaLucru.charAt(stivaLucru.length() - 3) != 'P') {
                bandaIntrare = stivaLucru.substring(poz + 3) + " " + bandaIntrare;
                stivaLucru = stivaLucru.substring(0, poz + 3).trim();
                pozitie--;
            }
            String number2 = stivaLucru.substring(poz + 1);
            int number = Integer.parseInt(stivaLucru.substring(poz + 1));

            stare = "q";
            //daca nr productiei are mai mult de o cifra
            stivaLucru = stivaLucru.substring(0, stivaLucru.length() - number2.length()) + String.valueOf(number + 1);
            removeSpaces();
//            String ceva1 = reguliProductie.get(number).getMembruDrept();
//            String ceva2 = bandaIntrare.substring(reguliProductie.get(number - 1).getMembruDrept().length()).trim();
            bandaIntrare = reguliProductie.get(number).getMembruDrept() + " " + bandaIntrare.substring(reguliProductie.get(number - 1).getMembruDrept().length()).trim();
        } else if (caz == 2) {
            stare = "e";
            stivaLucru = "eps";
            bandaIntrare = bandaIntrareInitial;
        } else {
            stare = "r";
//            stivaLucru = stivaLucru.substring(0, stivaLucru.length() - 1);
//            String membruStang = "";
//            for (RP rp : reguliProductie) {
//                if (rp.getMembruDrept().equals(String.valueOf(bandaIntrare.charAt(0)))) {
//                    membruStang = rp.getMembruStang();
//                }
//            }
//            removeSpaces();
//            bandaIntrare = membruStang + " " + bandaIntrare.substring(1);

            int poz = stivaLucru.lastIndexOf('P');
            int numar = Integer.parseInt(stivaLucru.substring(poz + 1));
            stivaLucru = stivaLucru.substring(0, poz).trim();
            int nr = reguliProductie.get(numar - 1).getMembruDrept().length();
            String secv = bandaIntrare.substring(0, nr);
//            bandaIntrare = reguliProductie.get(numar - 1).getMembruStang() + " " + bandaIntrare.substring(nr + 1).trim();
            stivaLucru = stivaLucru + " " + secv;
            bandaIntrare = reguliProductie.get(numar - 1).getMembruStang() + " " + bandaIntrare.substring(nr + 1).trim();

        }
        removeSpaces();
    }

    // (q, n+1, alfa, eps) |- (t, n+1, alfa, eps)
    String succes() {
        stare = "t";
        String[] sir = stivaLucru.split(" ");
        StringBuilder sirProductii = new StringBuilder();
        for (int i = 0; i < sir.length; i++) {
            if (sir[i].contains("P")) {
                sirProductii.append(sir[i]);
            }
        }
        return sirProductii.toString();
    }

    // (r, i, alfaa, beta) |- (r, i - 1, alfa, abeta)
    void revenire() {
        pozitie--;
        removeSpaces();
        bandaIntrare = stivaLucru.substring(stivaLucru.length() - 1) + " " + bandaIntrare;
        stivaLucru = stivaLucru.substring(0, stivaLucru.length() - 1);
        if (bandaIntrare.contains("eps")) {
            bandaIntrare = bandaIntrare.replace("eps", "");
            removeSpaces();
        }
    }


    boolean isExpandare() {
        //daca starea e q si banda de intrare incepe cu un neterminal
        return stare.equals("q") && Character.isUpperCase(bandaIntrare.charAt(0));
    }

    boolean isAvans() {
        //daca secventa data e parcursa deja integral
        if (pozitie > fip.size()) {
            return false;
        }
        //daca starea e q, banda de intrare inca are elemente, incepe cu terminal si acesta corespunde cu elementul de pe pozitia respectiva din secventa
        //terminal este codul din fip
        //return stare.equals("q") && !bandaIntrare.equals("eps") && !Character.isUpperCase(bandaIntrare.charAt(0)) && secventa.charAt(pozitie - 1) == bandaIntrare.charAt(0);
        String el = bandaIntrare.split(" ")[0];
        if (!Character.isDigit(el.charAt(0))) {
            return false;
        }
        int elem = Integer.parseInt(el);
        return stare.equals("q") && !bandaIntrare.equals("eps") && Character.isDigit(bandaIntrare.charAt(0)) && fip.get(pozitie - 1).getCod() == elem;
    }

    boolean isInsuccesDeMoment() {
        //daca secventa e deja parcursa si suntem in starea q
        if (pozitie > fip.size() && stare.equals("q")) {
            return true;
        }
        //daca e starea q si terminalul nu corespunde cu cel din secventa si
        int elem = Integer.parseInt(bandaIntrare.split(" ")[0]);
        return stare.equals("q") && fip.get(pozitie - 1).getCod() != elem;
    }

    boolean isSucces() {
        //verific daca terminalele din stiva de lucru corespund exact cu secventa data
//        String copie = stivaLucru;
//        for (int i = 0; i < stivaLucru.length(); i++) {
//            if (!Character.isDigit(stivaLucru.charAt(i))) {
//                if (!copie.isEmpty()) {
//                    copie = copie.substring(1);
//                } else {
//                    return false;
//                }
//            }
//        }
        //daca starea e q, in banda de intrare avem eps si nu mai exista niciun element in secventa nefolosit
//        return stare.equals("q") && bandaIntrare.equals("eps") && copie.length() == 0;
        return stare.equals("q") && bandaIntrare.equals("eps");
    }


    int isAltaIncercare() {
        if (stare.equals("r")) {
            //cazul 1 cand mai exista rp pe care o putem incerca
//            if (stivaLucru.charAt(stivaLucru.length() - 2) == 'P' || stivaLucru.charAt(stivaLucru.length() - 3) == 'P') {
            if (Character.isDigit(stivaLucru.charAt(stivaLucru.length() - 1))) {
                int poz = stivaLucru.lastIndexOf('P');
                String number2 = stivaLucru.substring(poz + 1);
                if (number2.contains(" ")) {
                    return 1;
                }
                int nr = Integer.parseInt(number2);
                if (gramatica.getRp().get(nr).getMembruStang().equals(gramatica.getRp().get(nr - 1).getMembruStang())) {
                    return 1;
                }
                return 3;
            }
            //cazul 2 cand i=1, A=S
            else if (pozitie == 1) {
                for (RP rp : gramatica.getRp()) {
                    if (rp.getMembruDrept().equals(bandaIntrare)) {
                        if (rp.getMembruStang().equals(bandaIntrareInitial)) {
                            return 2;
                        }
                    }
                }
            }
            //cazul 3, altfel
            else {
                return 3;
            }
        }
        return -1;
    }

    boolean isRevenire() {
        //daca starea e r
        int poz = stivaLucru.lastIndexOf('P');
        String secv = stivaLucru.substring(poz + 1);
        return stare.equals("r") && !Character.isDigit(secv.charAt(0));
    }

    public String getStare() {
        return stare;
    }

    //functie auxiliara pentru rezolvarea spatiilor
    void removeSpaces() {
        bandaIntrare = bandaIntrare.trim();
        stivaLucru = stivaLucru.trim();
        bandaIntrare = bandaIntrare.replace("  ", " ");
    }

    @Override
    public String toString() {
        return "AnalizatorDescReveniri{" +
                "stare='" + stare + '\'' +
                ", pozitie=" + pozitie +
                ", stivaLucru='" + stivaLucru + '\'' +
                ", bandaIntrare='" + bandaIntrare + '\'' +
                '}';
    }
}
