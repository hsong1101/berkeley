public class TriangleDrawer2 {

    public static void main(String[] args) {
        int col = 0;
        int row = 0;
        int SIZE = 10;


        for(int i = 1; i <= SIZE; i++){
            for(int j = 0; j < i; j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
