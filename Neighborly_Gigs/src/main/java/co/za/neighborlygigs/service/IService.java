package co.za.neighborlygigs.service;

public interface IService<T, ID> {
    T create(T entity);
    T read(ID id);
    T update(T entity);
    boolean delete(ID id);
}
