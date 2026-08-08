class Thermostat{
    private String location;
    private int temperature;

    private static final int MIN = 16;
    private static final int MAX = 30;

    private static int activeCount = 0;

    public Thermostat(String location, int startTemp){
        this.location = location;

        if (startTemp >= MIN && startTemp <= MAX){
            this.temperature = startTemp;
        }
        else{
            this.temperature = 22;
        }
        activeCount++;
    }

    public Thermostat(String location){
        this(location,22);
    }

    public void raise(){
        if (temperature < MAX){
            temperature++;
        }
        else{
            System.out.println("Already At Maximum (30)");
        }
    }

    public void lower(){
        if (temperature > MIN){
            temperature--;  
        }
        else{
            System.out.println("Already At Minimum (16)");
        }
    }

    public int getTemperature(){
        return  temperature;
    }

    public static int getActiveCount(){
        return activeCount;
    }
    public static void main(String args[]){
        Thermostat c1 = new Thermostat("Bedroom");
        Thermostat c2 = new Thermostat("Hall",20);
        System.out.println("Initial Temperature: " + c2.getTemperature());
        System.out.println("Raising Temperature....");
        for (int i = 0; i < 10; i++){
            c2.raise(); 
            System.out.println("Temperature: " + c2.getTemperature());
        }        
        System.out.println("Lowering Temperature....");
        for (int i = 0; i < 20; i++){
            c2.lower(); 
            System.out.println("Temperature: " + c2.getTemperature());
        }
        System.out.println("\nActive Thermostat :" + Thermostat.getActiveCount());
    }
}