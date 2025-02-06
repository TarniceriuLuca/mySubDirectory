package mySubDirectoryPackage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


public class Subscription {
    protected String name;
    protected float price;
    protected String billingPeriod; // monthly, yearly
    protected LocalDate startDate;
    protected int numberOfPayments;
    protected String type;

    public Subscription(String name, float price, String billingPeriod, LocalDate startDate, String type){
        this.name = name;
        this.price = price;
        this.billingPeriod = billingPeriod;
        this.startDate = startDate;
        updateNumberOfPayments();
        this.type = type;
    }


    public String getName() {
        return this.name;
    }
    public float getPrice() {
        return this.price;
    }
    public String getBillingPeriod() {
        return this.billingPeriod;
    }
    public int getNumberOfPayments() {
        return this.numberOfPayments;
    }


    /**
     * Functia atribuie campului numberOfPayments al obiectului valoarea necesara la momentul crearii obiectului.<br>
     * Aceasta este calculata folosind diferenta dintre data la momentul rularii programului si data de inceput
     */
    public void updateNumberOfPayments(){
        if(Objects.equals(this.getBillingPeriod(), "monthly")){
            this.numberOfPayments = (int)ChronoUnit.MONTHS.between(this.startDate, LocalDate.now()) + 1;
        }
        if(Objects.equals(this.getBillingPeriod(), "yearly")){
            this.numberOfPayments = (int)ChronoUnit.YEARS.between(this.startDate, LocalDate.now()) + 1;
        }
    }


    /**
     * Aceasta functie se foloseste de functia updateNumberOfPayments() si ia in considerare perioada de plata  din atributul "billingPeriod" pentru a calcula urmatoarea data la care trebuie platita subscriptia
     * @return LocalDate urmatoarea data la care trebuie platita subscriptia
     */
    public LocalDate nextBillingDate(){
        if(Objects.equals(this.getBillingPeriod(), "monthly"))
            return this.startDate.plusMonths(this.numberOfPayments);
        else
            return this.startDate.plusYears(this.numberOfPayments);
    }


    /**
     * Folosind nextBillingDate() arata daca urmatoarea plata este: in mai putin de "days" zile, exact in "days" zile, in mai mult de "days" zile sau astazi
     * @param days long numarul de zile pentru verificare
     * @return int <br>
     * -1 plata in mai putin de "days" zile <br>
     * 0 plata in exact "days" zile <br>
     * 1 plata in mai mult de "days" zile <br>
     * 2 plata astazi
     */
    public int isPaymentIn(long days){
        if(Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), this.nextBillingDate())) < days)
            return -1; // payment in less than <<days>>
        else if (Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), this.nextBillingDate())) == days)
            return 0; // payment in exactly <<days>>
        else if (LocalDate.now().getDayOfMonth() == this.startDate.getDayOfMonth())
            return 2; // payment today
        else
            return 1; // payment in more than <<days>>
    }



    @Override
    public String toString(){
        return this.name + " " + this.price + " " + this.billingPeriod + " " + this.startDate + " " + this.type;
    }
}
