public class SieveOfEratosthenes {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You need to enter an argument!");
        }else {
            int upperBound = Integer.parseInt(args[0]);
            boolean[] isNotPrime = new boolean[upperBound];

            for (int i = 2; i < upperBound; i++) {
                for (int j = 2; j <= i; j++) {
                    if (i != j && i % j == 0) {
                        isNotPrime[i] = true;
                        break;
                    }
                    isNotPrime[i] = false;
                }
            }
            for (int i = 2; i < upperBound; i++) {
                if (!isNotPrime[i]) {
                    System.out.println(i + " is a prime number.");
                }
            }
        }
    }
}