import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        Gramatica gramatica = new Gramatica();
        String fileName = "gramatica4.txt";
        gramatica.readFromFile2(fileName);
        List<FIP> fipList = readFIPFile();


        int cmd = 2;
        if (cmd == 1) {
            //teste: a, c, b, acbc, acbb, aacbac, aacbc
            String secventa = "acbc";

            AnalizatorDescReveniri analizator = new AnalizatorDescReveniri("q", 1, "eps", "P", gramatica, secventa);

            String sirProductii = "";
            while (!analizator.getStare().equals("e") && !analizator.getStare().equals("t")) {
                if (analizator.isExpandare()) {
                    analizator.expand();
                } else if (analizator.isAvans()) {
                    analizator.avans();
                } else if (analizator.isSucces()) {
                    sirProductii = analizator.succes();
                    break;
                } else if (analizator.isInsuccesDeMoment()) {
                    analizator.insuccesMoment();
                } else if (analizator.isRevenire()) {
                    analizator.revenire();
                } else if (analizator.isAltaIncercare() != -1) {
                    int caz = analizator.isAltaIncercare();
                    analizator.altaIncercare(caz);
                    if (caz == 2) {
                        break;
                    }
                    if (caz == 3) {
                        if (analizator.isRevenire()) {
                            analizator.revenire();
                        }
                    }
                }
            }

            if (sirProductii.isEmpty()) {
                System.out.println("Eroare");
            } else {
                System.out.println(sirProductii);
            }
        } else {

            AnalizatorDescReveniri2 analizator = new AnalizatorDescReveniri2("q", 1, "eps", "P", gramatica, fipList);

            String sirProductii = "";
            try {
            while (!analizator.getStare().equals("e") && !analizator.getStare().equals("t")) {
                if (analizator.isExpandare()) {
                    analizator.expand();
                } else if (analizator.isAvans()) {
                    analizator.avans();
                } else if (analizator.isSucces()) {
                    sirProductii = analizator.succes();
                    break;
                } else if (analizator.isInsuccesDeMoment()) {
                    analizator.insuccesMoment();
                } else if (analizator.isRevenire()) {
                    analizator.revenire();
                } else if (analizator.isAltaIncercare() != -1) {
                    int caz = analizator.isAltaIncercare();
                    analizator.altaIncercare(caz);
                    if (caz == 2) {
                        break;
                    }
                    if (caz == 3) {
                        if (analizator.isRevenire()) {
                            analizator.revenire();
                        }
                    }
                }
            }

            if (sirProductii.isEmpty()) {
                System.out.println("Eroare");
            } else {
                System.out.println(sirProductii);
            }
        } catch (Exception e) {
                System.out.println("Eroare");
            }
        }
    }

    static List<FIP> readFIPFile() {
        List<FIP> list = new ArrayList<>();
        String fileName = "FIP.txt";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));

            String row;
            while ((row = reader.readLine()) != null) {
                int first = 0;
                String atom = "";
                int cod = 0;
                for (int i = 0; i < row.length(); i++) {
                    if(row.charAt(i) == ' ' && first == 0) {
                        atom = row.substring(0, i);
                        first = 1;
                    } else if (row.charAt(i) == ' ' && first == 1 && row.charAt(i + 1) != ' ') {
                        cod = Integer.parseInt(row.substring(i + 1));
                        break;
                    }
                }
//                row = row.replace(" ", "-");
//                String atom = row.split("-")[0].trim();
//                int cod = Integer.parseInt(row.split("-")[1].trim());
                list.add(new FIP(atom, cod));
            }
            reader.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
