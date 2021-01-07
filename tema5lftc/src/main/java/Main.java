import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Gramatica gramatica = new Gramatica();
        String fileName = "gramatica3.txt";
        gramatica.readFromFile2(fileName);

        Scanner in = new Scanner(System.in);

//        AnalizatorDescReveniri analizator = new AnalizatorDescReveniri("q", 1, "eps", "S");
//        while(true) {
//            System.out.println("\n\n1. Multimea neterminalelor");
//            System.out.println("2. Multimea terminalelor");
//            System.out.println("3. Multimea regulilor de productie");
//            System.out.println("4. Multimea regulilor de productie pentru un terminal");
//            System.out.println("Introduceti comanda: ");
//            String cmd = in.nextLine();
//            switch(cmd) {
//                case "1":
//                    gramatica.printNeterminale();
//                    break;
//                case "2":
//                    gramatica.printTerminale();
//                    break;
//                case "3":
//                    gramatica.printRP();
//                    break;
//                case "4":
//                    System.out.println("Introduceti terminalul: ");
//                    String secv = in.nextLine();
//                    gramatica.printRPForSecv(secv);
//                    break;
//                default:
//                    break;
//            }
//        }
    }
}
