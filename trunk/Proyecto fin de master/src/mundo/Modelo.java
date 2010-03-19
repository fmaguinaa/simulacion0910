package mundo;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import managerXML.Acceso;
import managerXML.Contenido;
import managerXML.Punto;
import managerXML.Tramo;
import managerXML.XMLManager;
import managerXML.Atributo;


public class Modelo extends Observable {
	
	private ItemsMundo[][] contenido;
	private GeneradorVehiculos generador;
	private SemaforosManager semaforosManager;
	private Boolean parar;

	private Integer eleccion;
	private XMLManager manager;
	private Integer agresivos;
	private Integer normales;
	private Integer moderados;
	private Integer longitud;
	private Integer limite1;
	private Integer limite2;
	private Integer velocidadSimulacion;
	
	private ArrayList<Tramo> tramos;
	private ArrayList<Acceso> entradas;
	private ArrayList<Acceso> salidas;
	private ArrayList<Tramo> principales;
	private ArrayList<Tramo> horizontales;
	private ArrayList<Tramo> verticales;
	private ArrayList<Tramo> callejones;
	private ArrayList<Tramo> semaforos;
	private ArrayList<Punto> stops;
	private ArrayList<Punto> cedas;
	private ArrayList<Punto> crucesS;
	private ArrayList<Contenido> cruces; 
	private ArrayList<Contenido> campos;
	private ArrayList<Contenido> edificios;
	private ArrayList<Atributo> atributos;
	private ArrayList<Coche> coches;
	
	private File ficheroCampo;
	private File ficheroMapa;
	
	public void inicializar() {
		
		parar = false;
		velocidadSimulacion = 0;
		manager = new XMLManager();
		ficheroMapa = new File("./xml/Mapas/Inicializa1.xml");
		ficheroCampo = new File("./xml/mapas/Paisaje1.xml");
		manager.lanzarManager(ficheroMapa);
		atributos = manager.getAtributos();
		for (int i=0;i<atributos.size();i++) {
			Atributo atributo = atributos.get(i);
			longitud = atributo.getLongitud();
			limite1 = atributo.getLimite1();
			limite2 = atributo.getLimite2();
		}
		contenido = new ItemsMundo[longitud][longitud];
		coches = new ArrayList<Coche>();
		for (int i=0;i<longitud;i++)
			for (int j=0;j<longitud;j++) {
				if ((j<limite1) || (i<limite1) || (i>limite2) || (j>limite2)) 
					contenido[i][j] = new ItemsMundo(Constantes.BORDE,false);
				else
					contenido[i][j] = new ItemsMundo(Constantes.TIERRA,false);
			}
	}
	
	
	public void rellenarMatriz(Integer tipo,File fichero) {
		
		eleccion = tipo;		
		switch(tipo) {
		case 0:
			manager.lanzarManager(fichero);
			principales = manager.getPrincipales();
			horizontales = manager.getHorizontales();
			verticales = manager.getVerticales();
			callejones = manager.getCallejones();
			semaforos = manager.getSemaforos();
			stops = manager.getStops();
			cedas = manager.getCedas();
			crucesS = manager.getCrucesSimples();
			cruces = manager.getCruces();
			entradas = manager.getEntradas();
			salidas = manager.getSalidas();
			Ciudad ciudad = new Ciudad(this);
			ciudad.generarCiudad();
			break;
		case 1:
			manager.lanzarManager(fichero);
			tramos = manager.getTramos();
			entradas = manager.getEntradas();
			salidas = manager.getSalidas();
			manager.lanzarManager(ficheroCampo);
			campos = manager.getCampos();
			edificios = manager.getEdificios();
			Autovia autovia = new Autovia(this);
			autovia.generarAutovia();
			break;
		case 2:
			manager.lanzarManager(fichero);
			tramos = manager.getTramos();
			entradas = manager.getEntradas();
			salidas = manager.getSalidas();
			manager.lanzarManager(ficheroCampo);
			campos = manager.getCampos();
			edificios = manager.getEdificios();
			Secundaria secundaria = new Secundaria(this);
			secundaria.generarSecundaria();
			break;
		}
		actualizar();
	}
	
	public String obtenerDireccion(int dir) {
		
		String valor = null;
		switch(dir) {
		case 0:
			valor = Constantes.ARRIBA;
			break;
		case 1:
			valor = Constantes.ABAJO;
			break;
		case 2:
			valor = Constantes.IZQUIERDA;
			break;
		case 3:
			valor = Constantes.DERECHA;
			break;
		}
		return valor;
	}

	public void simular() {
	
		generador = new GeneradorVehiculos(this);
		generador.start();
		if (eleccion == 0) {
			semaforosManager = new SemaforosManager(this);
			semaforosManager.start();
		}
	}

	public void actualizar() {
	
		setChanged();
		notifyObservers();
	}

	public void obtenerConductores(Integer agre, Integer norm,
		Integer mod) {
	
		agresivos = agre;
		normales = norm;
		moderados = mod;
	}

	public ArrayList<Tramo> getTramos() {
		return tramos;
	}


	public void setTramos(ArrayList<Tramo> tramos) {
		this.tramos = tramos;
	}


	public ArrayList<Acceso> getEntradas() {
		return entradas;
	}


	public void setEntradas(ArrayList<Acceso> entradas) {
		this.entradas = entradas;
	}


	public ArrayList<Acceso> getSalidas() {
		return salidas;
	}


	public void setSalidas(ArrayList<Acceso> salidas) {
		this.salidas = salidas;
	}


	public ArrayList<Tramo> getPrincipales() {
		return principales;
	}


	public void setPrincipales(ArrayList<Tramo> principales) {
		this.principales = principales;
	}


	public ArrayList<Tramo> getHorizontales() {
		return horizontales;
	}


	public void setHorizontales(ArrayList<Tramo> horizontales) {
		this.horizontales = horizontales;
	}


	public ArrayList<Tramo> getVerticales() {
		return verticales;
	}


	public void setVerticales(ArrayList<Tramo> verticales) {
		this.verticales = verticales;
	}


	public ArrayList<Tramo> getCallejones() {
		return callejones;
	}


	public void setCallejones(ArrayList<Tramo> callejones) {
		this.callejones = callejones;
	}


	public ArrayList<Tramo> getSemaforos() {
		return semaforos;
	}


	public void setSemaforos(ArrayList<Tramo> semaforos) {
		this.semaforos = semaforos;
	}


	public ArrayList<Punto> getStops() {
		return stops;
	}


	public void setStops(ArrayList<Punto> stops) {
		this.stops = stops;
	}


	public ArrayList<Punto> getCedas() {
		return cedas;
	}


	public void setCedas(ArrayList<Punto> cedas) {
		this.cedas = cedas;
	}


	public ArrayList<Punto> getCrucesS() {
		return crucesS;
	}


	public void setCrucesS(ArrayList<Punto> crucesS) {
		this.crucesS = crucesS;
	}


	public ArrayList<Contenido> getCruces() {
		return cruces;
	}


	public void setCruces(ArrayList<Contenido> cruces) {
		this.cruces = cruces;
	}


	public ArrayList<Contenido> getCampos() {
		return campos;
	}


	public void setCampos(ArrayList<Contenido> campos) {
		this.campos = campos;
	}


	public ArrayList<Contenido> getEdificios() {
		return edificios;
	}


	public void setEdificios(ArrayList<Contenido> edificios) {
		this.edificios = edificios;
	}


	public Integer getVelocidadSimulacion() {
		
		return velocidadSimulacion;
	}

	public void setVelocidadSimulacion(Integer velocidadSimulacion) {
		
		this.velocidadSimulacion = velocidadSimulacion;
	}

	public void setParar(boolean stop) {
		
		parar = stop;
	}
	
	public boolean getParar() {
		
		return parar;
	}
	
	public GeneradorVehiculos getGenerador() {
		return generador;
	}

	public void setGenerador(GeneradorVehiculos generador) {
		this.generador = generador;
	}

	public Integer getEleccion() {
		return eleccion;
	}

	public void setEleccion(Integer eleccion) {
		this.eleccion = eleccion;
	}

	public XMLManager getManager() {
		return manager;
	}

	public void setManager(XMLManager manager) {
		this.manager = manager;
	}

	public Integer getAgresivos() {
		return agresivos;
	}

	public void setAgresivos(Integer agresivos) {
		this.agresivos = agresivos;
	}

	public Integer getNormales() {
		return normales;
	}

	public void setNormales(Integer normales) {
		this.normales = normales;
	}

	public Integer getModerados() {
		return moderados;
	}

	public void setModerados(Integer moderados) {
		this.moderados = moderados;
	}

	public Integer getLongitud() {
		return longitud;
	}

	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
	}

	public Integer getLimite1() {
		return limite1;
	}

	public void setLimite1(Integer limite1) {
		this.limite1 = limite1;
	}

	public Integer getLimite2() {
		return limite2;
	}

	public void setLimite2(Integer limite2) {
		this.limite2 = limite2;
	}

	public void setContenido(ItemsMundo[][] contenido) {
		this.contenido = contenido;
	}

	public void setCoches(ArrayList<Coche> coches) {
		this.coches = coches;
	}

	public void setParar(Boolean parar) {
		this.parar = parar;
	}
	
	public ItemsMundo[][] getContenido() {
	
		return contenido;
	}
	
	public ItemsMundo getItem(int i,int j) {
		
		return contenido[i][j];
	}
	
	public ArrayList<Coche> getCoches() {
		
		return coches;
	}
}
