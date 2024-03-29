package mundo;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import agente.Conductor;

import manager.Acceso;
import manager.Atributo;
import manager.Contenido;
import manager.Estadisticas;
import manager.Punto;
import manager.Tramo;
import manager.XMLManager;


public class Entorno extends Observable {
	
	private ItemsMundo[][] contenido;
	private GeneradorTrafico generador;
	private SemaforosManager semaforosManager;
	private Boolean parar;
	private Boolean tipoVehiculos;

	private Integer eleccion;
	private XMLManager manager;
	private Integer agresivos;
	private Integer normales;
	private Integer moderados;
	private Integer longitud;
	private Integer limite1;
	private Integer limite2;
	private Integer velocidadSimulacion;
	private Integer longitudSimulacion;
	private Integer impaciencia;

	private ArrayList<Punto> inicios;
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
	private ArrayList<Conductor> conductores;
	private ArrayList<Punto> comienzoVueltas;
	
	private File ficheroCampo;
	private File ficheroMapa;
	
	private Estadisticas estadisticas;
	
	public Entorno() {
	
		estadisticas = new Estadisticas();
	}
	
	public void inicializar() {
	
		parar = false;
		tipoVehiculos = false;
		velocidadSimulacion = 0;
		impaciencia = 0;
		longitudSimulacion = 0;
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
		conductores = new ArrayList<Conductor>();
		inicios = new ArrayList<Punto>();
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
			comienzoVueltas = manager.getComienzoVueltas();
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
			comienzoVueltas = manager.getComienzoVueltas();
			Secundaria secundaria = new Secundaria(this);
			secundaria.generarSecundaria();
			break;
		}
		if (!encontrarInicios() && !margenesCorrectos()) {
			setParar(true);
			actualizar(1);
		}
		else
			actualizar(0);
	}
	
	public boolean encontrarInicios() {
		
		Boolean encontrado = false;
		for (int i=longitud-1;i>=0;i--)
			for (int j=longitud-1;j>=0;j--) 
				if (contenido[i][j].isInicio()) {
					Punto p = new Punto(i,j);
					inicios.add(p);
					encontrado = true;
				}
		return encontrado;
	}
	
	public boolean margenesCorrectos() {
		
		Boolean encontrado1 = false;
		Boolean encontrado2 = false;
		for (int i=0;i<longitud && !encontrado1;i++)
			for (int j=0;j<6 && !encontrado1;j++)
				if (!contenido[i][j].getTipo().equals(Constantes.CAMPO) &&
						!contenido[i][j].getTipo().equals(Constantes.EDIFICIO) &&
						!contenido[i][j].getTipo().equals(Constantes.BORDE) &&
						!contenido[i][j].getTipo().equals(Constantes.TIERRA))
						encontrado1 = true;
		for (int i=0;i<6 && !encontrado2;i++)
			for (int j=0;j<longitud && !encontrado2;j++)
				if (!contenido[i][j].getTipo().equals(Constantes.CAMPO) &&
						!contenido[i][j].getTipo().equals(Constantes.EDIFICIO) &&
						!contenido[i][j].getTipo().equals(Constantes.BORDE) &&
						!contenido[i][j].getTipo().equals(Constantes.TIERRA))
						encontrado2 = true;			
		return (encontrado1 == false && encontrado2 == false);
	}
	
	public void salida(int x1,int x2,int x3,int x4,int y1,int y2,
			int tram1,int tram2,int dir1,int dir2) {
		
		for (int i=x1;i<x2;i++) {
			contenido[i][y1].setTipo(Constantes.CARRIL_SALIDA);
			contenido[i][y1].setTramo(tram1);
			contenido[i][y1].setDireccion(obtenerDireccion(dir1));
			contenido[i][y1].setSalida(true);
		}
	
		for (int j=x3;j<x4;j++) {
			contenido[y2][j].setTipo(Constantes.CARRIL_SALIDA);
			contenido[y2][j].setTramo(tram2);
			contenido[y2][j].setDireccion(obtenerDireccion(dir2));
			contenido[y2][j].setSalida(true);
		}
	}
	
	public void entrada(int x1,int x2,int x3,int x4,int y1,int y2,int iniciox,int inicioy,
			int tram1,int tram2,int dir1,int dir2) {
		
		for (int i=x1;i<x2;i++) {
			contenido[i][y1].setTipo(Constantes.CARRIL_ENTRADA); 
			contenido[i][y1].setTramo(tram1);
			contenido[i][y1].setDireccion(obtenerDireccion(dir1));
		}
	
		for (int j=x3;j<x4;j++) {
			contenido[y2][j].setTipo(Constantes.CARRIL_ENTRADA);
			contenido[y2][j].setTramo(tram2);
			contenido[y2][j].setDireccion(obtenerDireccion(dir2));
		}
		contenido[iniciox][inicioy].setInicio(true);
	}

	public void simular() {
	
		estadisticas.resetear();
		generador = new GeneradorTrafico(this);
		generador.start();
		if (eleccion == 0) {
			semaforosManager = new SemaforosManager(this);
			semaforosManager.start();
		}
	}

	public void actualizar(Integer valor) {
	
		setChanged();
		notifyObservers(valor);
	}

	public void obtenerConductores(Integer agre, Integer norm,
		Integer mod) {
	
		agresivos = agre;
		normales = norm;
		moderados = mod;
	}
	
	public void obtenerTipoVehiculos(boolean seleccion) {
		
		tipoVehiculos = seleccion;
	}

	public void obtenerImpaciencia(Integer seleccion) {
		
		impaciencia = seleccion;
	}
	
	public Integer getLongitudSimulacion() {
		
		return longitudSimulacion;
	}
	
	public void setLongidudSimulacion(Integer seleccion) {
	
		longitudSimulacion = seleccion;
		
	}
	
	public Estadisticas getEstadisticas() {
		return estadisticas;
	}

	public Integer getImpaciencia() {
		return impaciencia;
	}

	public Boolean getTipoVehiculos() {
		return tipoVehiculos;
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
	
	public GeneradorTrafico getGenerador() {
		return generador;
	}

	public void setGenerador(GeneradorTrafico generador) {
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

	public void setCoches(ArrayList<Conductor> conductores) {
		this.conductores = conductores;
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
	
	public ArrayList<Punto> getInicios() {
		return inicios;
	}

	public ArrayList<Conductor> getConductores() {
		
		return conductores;
	}
	
	public ArrayList<Punto> getComienzoVueltas() {
		
		return comienzoVueltas;
	}
	
	private String obtenerDireccion(int dir) {
		
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
}
