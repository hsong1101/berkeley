public class GregorianDate extends Date {

    public static int[] monthLengths = {31, 28, 31, 30, 31, 30, 31,
        31, 30, 31, 30, 31};

    public GregorianDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }

    @Override
    public Date nextDate() {
        if (this.month() == 12 && this.dayOfMonth() == 31) {
            return new GregorianDate(this.year()+1, 1, 1);
        } else if (this.dayOfMonth() == monthLengths[this.month()] ) {
            return new GregorianDate(this.year(), this.month() + 1, 1);
        } else {
            return new GregorianDate(this.year(), this.month(), dayOfMonth()+1);
        }
    }

    @Override
    public int dayOfYear() {
        int rtnValue = 0;
        for (int m = 0; m < month() - 1; m++) {
            rtnValue += monthLengths[m];
        }
        return rtnValue + dayOfMonth();
    }

}
