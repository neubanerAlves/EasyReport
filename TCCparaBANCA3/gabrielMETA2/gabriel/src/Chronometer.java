//Publicado por mcostabr em: http://www.guj.com.br/java/73921-marcar-tempo-de-execucao-de-um-metodo

public final class Chronometer  
{  
    private static long msBegin,msEnd,msTime;  
  
    private Chronometer() {}  
  
    public static void start()  
    {  
    	msTime = 0;  
        msBegin = System.currentTimeMillis();  
    }  
  
    public static void stop()  
    {  
        msEnd = System.currentTimeMillis();  
        msTime += msEnd-msBegin;  
    }  
  
    public static void resume()  
    {  
        msBegin = System.currentTimeMillis();  
    }  
  
    public static long time()  
    {  
        return msTime;  
    }  
  
    public static double stime()  
    {  
        return msTime/1000.;  
    }  
  
    public static double mtime()  
    {  
        return msTime/60000.;  
    }  
  
    public static double htime()  
    {  
        return msTime/3600000.;  
    }  
  
}  