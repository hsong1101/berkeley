import java.util.HashMap;

public class Fibonacci {
	int callsToFib;
	int result;

	HashMap<Integer, Integer> fib = new HashMap<>();

	public Fibonacci(int n){
		this.callsToFib = 0;
		this.result = fib(n);
	}

	//fib.put(n, result);
	
	private int fib(int n) {
		callsToFib++;
		if (n == 0) {
			return 0;
		} else if (n == 1) {
			return 1;
		} else {

			if(fib.containsKey(n)) {
				return fib.get(n);
			}else {
				int returnValue = fib(n - 1) + fib(n - 2);
				fib.put(n, returnValue);
				return returnValue;
			}
		}
	}

}
