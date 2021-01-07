import java.util.List;

public class AnalizatorDescReveniri {
    private String stare;
    private int pozitie;
    private String stivaLucru;
    private String bandaIntrare;
    private String bandaIntrareInitial;
    private Gramatica gramatica;
    private String secventa;

    AnalizatorDescReveniri(String stare, int pozitie, String stivaLucru, String bandaIntrare, Gramatica gramatica, String secventa) {
        this.stare = stare;
        this.pozitie = pozitie;
        this.stivaLucru = stivaLucru;
        this.bandaIntrare = bandaIntrare;
        this.bandaIntrareInitial = bandaIntrare;
        this.gramatica = gramatica;
        this.secventa = secventa;
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

    // (q, i, alfa, Abeta) |- (q, i, alfaA(1), gama(i)beta)
    void expand() {
        RP rp = gramatica.getRp().get(0);
        if (stivaLucru.equals("eps")) {
            stivaLucru = String.valueOf(rp.getNumber());
        } else {
            stivaLucru = stivaLucru.concat(String.valueOf(rp.getNumber()));
        }
        removeSpaces();
        bandaIntrare = rp.getMembruDrept() + " " + bandaIntrare.substring(1);
        removeSpaces();
    }

    // (q, i, alfa, a(i)beta) |- (q, i + 1, alfaa(i), beta)
    void avans() {
        pozitie++;
        removeSpaces();
        stivaLucru = stivaLucru + bandaIntrare.charAt(0);
        bandaIntrare = bandaIntrare.substring(1);
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
            int number = Integer.parseInt(stivaLucru.substring(stivaLucru.length() - 1));
            stare = "q";
            stivaLucru = stivaLucru.substring(0, stivaLucru.length() - 1) + String.valueOf(number + 1);
            removeSpaces();
            bandaIntrare = reguliProductie.get(number).getMembruDrept() + " " + bandaIntrare.substring(reguliProductie.get(number - 1).getMembruDrept().length());
        } else if (caz == 2) {
            stare = "e";
            stivaLucru = "eps";
            bandaIntrare = bandaIntrareInitial;
        } else {
            stare = "r";
            stivaLucru = stivaLucru.substring(0, stivaLucru.length() - 1);
            String membruStang = "";
            for (RP rp : reguliProductie) {
                if (rp.getMembruDrept().equals(String.valueOf(bandaIntrare.charAt(0)))) {
                    membruStang = rp.getMembruStang();
                }
            }
            removeSpaces();
            bandaIntrare = membruStang + " " + bandaIntrare.substring(1);
        }
        removeSpaces();
    }

    // (q, n+1, alfa, eps) |- (t, n+1, alfa, eps)
    String succes() {
        stare = "t";
        StringBuilder sirProductii = new StringBuilder();
        for (int i = 0; i < stivaLucru.length(); i++) {
            if (Character.isDigit(stivaLucru.charAt(i))) {
                sirProductii.append(stivaLucru.charAt(i));
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
        if (pozitie > secventa.length()) {
            return false;
        }
        //daca starea e q, banda de intrare inca are elemente, incepe cu terminal si acesta corespunde cu elementul de pe pozitia respectiva din secventa
        return stare.equals("q") && !bandaIntrare.equals("eps") && Character.isLowerCase(bandaIntrare.charAt(0)) && secventa.charAt(pozitie - 1) == bandaIntrare.charAt(0);
    }

    boolean isInsuccesDeMoment() {
        //daca secventa e deja parcursa si suntem in starea q
        if (pozitie > secventa.length() && stare.equals("q")) {
            return true;
        }
        //daca e starea q si terminalul nu corespunde cu cel din secventa si
        return stare.equals("q") && secventa.charAt(pozitie - 1) != bandaIntrare.charAt(0);
    }

    boolean isSucces() {
        //verific daca terminalele din stiva de lucru corespund exact cu secventa data
        String copie = secventa;
        for (int i = 0; i < stivaLucru.length(); i++) {
            if (!Character.isDigit(stivaLucru.charAt(i))) {
                if (!copie.isEmpty()) {
                    copie = copie.substring(1);
                } else {
                    return false;
                }
            }
        }
        //daca starea e q, in banda de intrare avem eps si nu mai exista niciun element in secventa nefolosit
        return stare.equals("q") && bandaIntrare.equals("eps") && copie.length() == 0;
    }


    int isAltaIncercare() {
        if (stare.equals("r")) {

            char number = stivaLucru.charAt(stivaLucru.length() - 1);
            //cazul 1 cand mai exista rp pe care o putem incerca
            if (Character.isDigit(number) && Integer.parseInt(String.valueOf(number)) < gramatica.getRp().size()) {
                return 1;
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
        return stare.equals("r") && !Character.isDigit(stivaLucru.charAt(stivaLucru.length() - 1));
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
