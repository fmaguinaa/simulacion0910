package mundo;


public class Coche extends Thread {
	
	
	
	private ItemsMundo[][] contenido;
	private Matriz matriz;
	private ItemsMundo anterior;
	private Integer x;
	private Integer y;
	private Integer antX;
	private Integer antY;
	private Integer velocidad;
	private String direccion;
	
	public Coche(Matriz matrix,Integer comienzoX,Integer comienzoY,Integer velocidadIni) {
		
		matriz = matrix;
		contenido = matriz.getContenido();
		antX = comienzoX;
		antY = comienzoY;
		x = antX;
		y = antY;
		velocidad = velocidadIni;
		direccion = contenido[antX][antY].getDireccion();
		anterior = new ItemsMundo(contenido[antX][antY].getTipo(),contenido[antX][antY].isInicio());
		contenido[antX][antY].setTipo(Constantes.AUTOMOVIL);
	}
	
	public Integer getVelocidad() {
		
		return velocidad;
	}
	
	public void setVelocidad(Integer velAct) {
		
		velocidad = velAct;
	}
	
	public String getDireccion() {
		
		return direccion;
	}
	
	public void setDireccion(String sentido) {
		
		direccion = sentido;
	}
	
	public void setAnterior(ItemsMundo ant) {
		
		anterior = ant;
	}
	
	public ItemsMundo getAnterior() {
		
		return anterior;
	}
	
	public void avanzar() {
		
		contenido[antX][antY].setTipo(anterior.getTipo());
		//System.out.println("X: "+ antX+" Y: "+antY+ "Aux: "+aux);
		anterior.setTipo(contenido[x][y].getTipo());
		//System.out.println("Aux nuevo: "+aux);
		contenido[x][y].setTipo(Constantes.AUTOMOVIL);
		//System.out.println("X actual: "+ x+" Y actual: "+y+ ": "+contenido[x][y].toString());
		antX = x;
		antY = y;
		if (anterior.getTipo().contains("Autovia") || anterior.getTipo().contains("Secundaria") || 
				anterior.getTipo().contains("Calle")) {
			if (contenido[x][y].getDireccion().equals(Constantes.DERECHA)) {
				y = y+1;
				direccion = Constantes.DERECHA;
			}
			else if (contenido[x][y].getDireccion().equals(Constantes.IZQUIERDA)) {
				y = y-1;
				direccion = Constantes.IZQUIERDA;
			}
			else if (contenido[x][y].getDireccion().equals(Constantes.ARRIBA)) {
				x = x-1;
				direccion = Constantes.ARRIBA;
			}
			else if (contenido[x][y].getDireccion().equals(Constantes.ABAJO)) {
				x = x+1;
				direccion = Constantes.ABAJO;
			}
		}
		else if (anterior.getTipo().contains("Cruce") || anterior.getTipo().contains("Semaforo")) {
			if (anterior.getDireccion().equals(Constantes.ABAJO))
				x = x+1;
			else if (direccion.equals(Constantes.ARRIBA))
				x = x-1;
			else if (direccion.equals(Constantes.DERECHA))
				y = y+1;
			else if (direccion.equals(Constantes.IZQUIERDA))
				y = y-1;
		}
		else if (anterior.getTipo().equals(Constantes.CARRIL_ENTRADA)) {
			if (!(contenido[x-1][y].getTipo().equals(Constantes.CARRIL_ENTRADA)) &&
					!(contenido[x-1][y].getTipo().equals(Constantes.AUTOMOVIL)) && 
					!(contenido[x][y-1].getTipo().equals(Constantes.AUTOMOVIL)))
				y = y-1;
			else if (!(contenido[x-1][y].getTipo().equals(Constantes.AUTOMOVIL)) && 
					(contenido[x-1][y].getDireccion().equals(Constantes.ARRIBA)))
				x = x-1;
			else if (!(contenido[x][y-1].getTipo().equals(Constantes.AUTOMOVIL)) &&
					(contenido[x][y-1].getDireccion().equals(Constantes.IZQUIERDA)))
				y = y-1;
		}
		/*else if (siguiente.equals(AUTOMOVIL))
			tratarAdelantamiento();
		*/	
	}
	
	private void tratarAdelantamiento() {
		
		if (direccion.equals(Constantes.ABAJO))
			y = y+1;
		else if (direccion.equals(Constantes.ARRIBA))
			y = y-1;
		else if (direccion.equals(Constantes.DERECHA))
			x = x-1;
		else if (direccion.equals(Constantes.IZQUIERDA))
			x = x+1;
		
	}
	
	public void run() {
		
		while(!matriz.getParar()) {
			avanzar();
			matriz.actualizar();
			try {
					Coche.sleep(300-velocidad);
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
