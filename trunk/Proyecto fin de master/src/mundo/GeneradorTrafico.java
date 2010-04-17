package mundo;

import java.util.ArrayList;
import java.util.Random;

import managerXML.Punto;

import agente.Conductor;

public class GeneradorTrafico extends Thread {
	
	private static final Integer VCiudad = 30;
	private static final Integer VAuto = 120;
	private static final Integer VSec = 100;
	
	private Integer eleccion;
	private Entorno entorno;
	private Integer moderados;
	private Integer normales;
	private Integer agresivos;
	private Integer vIni;
	private ArrayList<Punto> inicios;;
	
	public GeneradorTrafico(Entorno mundo) {
		
		entorno = mundo;
		eleccion = entorno.getEleccion();
		moderados = entorno.getModerados();
		normales = entorno.getNormales();
		agresivos = entorno.getAgresivos();
		inicios = entorno.getInicios();
		vIni = 0;
	}
	
	public void run() {
			
		Random impaciencia = new Random();
		if (eleccion == 0)
			vIni = VCiudad;
		else if (eleccion == 1) 
			vIni = VAuto;
		else if (eleccion == 2)
			vIni = VSec;
		
		while (!(entorno.getParar())
		&&(moderados > 0 || normales > 0 || agresivos > 0))
			if (moderados > 0) {
				for (int i=0;i<inicios.size();i++) {
					Punto p = inicios.get(i);
					Coche coche = new Coche(entorno,vIni,vIni);
					Conductor conductor = new Conductor(entorno,Constantes.MODERADO,impaciencia.nextInt(3)+1,coche,p.getX(),p.getY());
					entorno.getConductores().add(conductor);
					entorno.actualizar(0);
					conductor.start();	
				}
				moderados = moderados - 1;
				try {
					GeneradorTrafico.sleep(2500 -
						entorno.getVelocidadSimulacion());
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			else if (normales > 0) {
				for (int i=0;i<inicios.size();i++) {
					Punto p = inicios.get(i);
					Coche coche = new Coche(entorno,vIni,vIni);
					Conductor conductor = new Conductor(entorno,Constantes.NORMAL,impaciencia.nextInt(3)+1,coche,p.getX(),p.getY());
					entorno.getConductores().add(conductor);
					entorno.actualizar(0);
					conductor.start();
				}
				normales = normales - 1;
				try {
					GeneradorTrafico.sleep(2500 - 
						entorno.getVelocidadSimulacion());
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}			
			else if (agresivos > 0) {
				for (int i=0;i<inicios.size();i++) {
					Punto p = inicios.get(i);
					Coche coche = new Coche(entorno,vIni,vIni);
					Conductor conductor = new Conductor(entorno,Constantes.AGRESIVO,impaciencia.nextInt(3)+1,coche,p.getX(),p.getY());
					entorno.getConductores().add(conductor);
					entorno.actualizar(0);
					conductor.start();
				}
				agresivos = agresivos - 1;
				try {
					GeneradorTrafico.sleep(2500 - 
						entorno.getVelocidadSimulacion());
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}			
			finalizar();
	}
	
	public void finalizar() {
		
		try {
			this.finalize();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

}