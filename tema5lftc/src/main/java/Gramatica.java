import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Gramatica {
    private List<String> terminale = new ArrayList<>();
    private List<String> neterminale = new ArrayList<>();
    private List<RP> rp = new ArrayList<>();


    public void readFromFile(String fileName) {
        BufferedReader reader = null;
        int number = 1;

        try {
            reader = new BufferedReader(new FileReader(fileName));

            String row;
            while ((row = reader.readLine()) != null) {
                String membruStang = row.split("->")[0].trim();
                String membruDrept = row.split("->")[1].trim();
                getTerminaleNeterminale(membruStang);

                if (membruDrept.contains("|")) {
                    for (String seq : membruDrept.split("\\|")) {
                        rp.add(new RP(membruStang, seq.trim(), number));
                        getTerminaleNeterminale(seq.trim());
                        number++;
                    }
                } else {
                    rp.add(new RP(membruStang, membruDrept, number));
                    getTerminaleNeterminale(membruDrept);
                    number++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void readFromFile2(String fileName) {
        BufferedReader reader = null;
        int number = 1;
        try {
            reader = new BufferedReader(new FileReader(fileName));

            String row;
            while ((row = reader.readLine()) != null) {
                String membruStang = row.split("->")[0].trim();
                String membruDrept = row.split("->")[1].trim();
                getTerminaleNeterminale2(membruStang);

                if (membruDrept.contains("|")) {
                    for (String seq : membruDrept.split("\\|")) {
                        rp.add(new RP(membruStang, seq.trim(), number));
                        getTerminaleNeterminale2(seq.trim());
                        number++;
                    }
                } else {
                    rp.add(new RP(membruStang, membruDrept, number));
                    getTerminaleNeterminale2(membruDrept);
                    number++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void getTerminaleNeterminale2(String secv) {
        String[] pars = secv.split(" ");
        for (String s : pars) {
            char ch = s.charAt(0);
            if (Character.isUpperCase(ch)) {
                if (!neterminale.contains(s) && !s.equals("ID") && !s.equals("CONST"))
                    neterminale.add(s);
                else if (s.equals("ID") || s.equals("CONST")) {
                    if (!terminale.contains(s))
                        terminale.add(s);
                }
            } else if (Character.isLowerCase(ch)) {
                if (!terminale.contains(s))
                    terminale.add(s);
            } else {
                if (!terminale.contains(s))
                    terminale.add(s);
            }
        }

    }

    void getTerminaleNeterminale(String secv) {
//        for (int i = 0; i < secv.length(); i++) {
//            char ch = secv.charAt(i);
//            if (Character.isUpperCase(ch)) {
//                if (!neterminale.contains(ch))
//                    neterminale.add(ch);
//            } else {
//                if (!terminale.contains(ch))
//                    terminale.add(ch);
//            }
//        }
    }

    public void printNeterminale() {
        for (int i = 0; i < neterminale.size(); i++) {
            System.out.print(neterminale.get(i) + " ");
        }
    }

    public void printTerminale() {
        for (int i = 0; i < terminale.size(); i++) {
            System.out.print(terminale.get(i) + " ");
        }
    }

    public void printRP() {
        for (int i = 0; i < rp.size(); i++) {
            System.out.println(rp.get(i));
        }
    }

    public void printRPForSecv(String secventa) {
        if (Character.isUpperCase(secventa.charAt(0))) {
            System.out.println("Este un neterminal");
            return;
        }
        for (int i = 0; i < rp.size(); i++) {
            if (rp.get(i).getMembruDrept().contains(secventa) || rp.get(i).getMembruStang().contains(secventa))
                System.out.println(rp.get(i));
        }
    }

    public List<RP> getRp() {
        return rp;
    }
}
