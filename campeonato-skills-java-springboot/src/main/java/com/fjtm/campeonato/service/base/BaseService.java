
package com.fjtm.campeonato.service.base;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<T, ID, R extends JpaRepository<T, ID>> {

	@Autowired
	protected R repositorio;
	
	public T save(T t) {
		return repositorio.save(t);
	}
	
	public Optional<T> findById(ID id) {
		return repositorio.findById(id);
	}
	
	public List<T> findAll() {
		return repositorio.findAll();
	}
	
	// esta funcion no tengo claro que hace pero me parece que devuelve un listado de todos los productos
	// y luego se le pasa un parametro pageable que es un objeto que contiene el numero de pagina y el tamaño de la pagina
	// y devuelve un objeto de tipo Page<T> que es una clase que contiene la informacion de la pagina
	// como numero de pagina, numero de paginas, tamaño de la pagina, etc
	// la paginacion se hace con el parametro pageable
	// el parametro pageable tiene un objeto de tipo Pageable que contiene el numero de pagina y el tamaño de la pagina
	public Page<T> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}
	
	public T edit(T t) {
		return repositorio.save(t);
	}
	
	public void delete(T t) {
		repositorio.delete(t);
	}
	
	public void deleteById(ID id) {
		repositorio.deleteById(id);
	}
	
	
}