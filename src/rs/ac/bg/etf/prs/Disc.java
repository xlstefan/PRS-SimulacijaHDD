package rs.ac.bg.etf.prs;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Sqrt;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Project name: PRS-SimulacijaHDD
 * Created by Stefan on 20-Sep-17.
 */
public class Disc implements Observable{

    /**
     * Revolutions per minute
     */
    private  int rpm;
    private  int cylinders;

    private List<Observer> observers;

    private Double sizeOfOneRecord;

    private boolean busy;
    private static int cylinder = 100;

    public Disc(int rpm, int cylinders, Double sizeOfOneRecord) {
        this.rpm = rpm;
        this.cylinders = cylinders;
        this.sizeOfOneRecord = sizeOfOneRecord;
        this.busy = false;
        observers = new ArrayList<>();
    }

    /**
     * @return 60/RPM(of the disc)
     */
    public double Trev(){
        return 60.0/rpm;
    }

    private Double calculateIntegral(double request){
        return (2.0/Math.pow(cylinder,2))*
                Math.abs(cylinder-request)*
                (Math.pow(request,1.5)/(1.5));
    }


    public List<Double> receiveRequest(Request request) {
        int requestId = request.getId();
        Double Tam = calculateIntegral(request.getLength());
        Double Trd = (1.0/2.0)*Trev(); // odredi po formuli
        Double Ttr = (sizeOfOneRecord)*Trev();
        Double Tuk = Tam + Trd + Ttr;
        List<Double> list = new ArrayList<>();
        list.add(Tam);
        list.add(Trd);
        list.add(Ttr);
        notifyObserver(requestId);
        return list;
    }

    public boolean isBusy() {
        return busy;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver(int reqId) {
        for(Observer o: observers){
            o.update(reqId);
        }
    }
}
