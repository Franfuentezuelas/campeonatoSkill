package com.fjtm.campeonato.error;

public class NotFoundException extends RuntimeException {

    //private static final Long serialVersionUID = -1l;

    private final Long id;
    private final String info;
    private final String ruta;

    public NotFoundException(Long id, String info, String ruta) {
        super(id + " no encontrado.");
        this.id = id;
        this.info = info;
        this.ruta = ruta;
    }
    public NotFoundException(String info, String ruta) {
        super("No encontrados parametros.");
        this.id = null;
        this.info = info;
        this.ruta = ruta;
    }

    public Long getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }
    public String getRuta() {
        return ruta;
    }
}
