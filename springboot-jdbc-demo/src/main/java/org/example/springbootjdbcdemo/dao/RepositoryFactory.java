package org.example.springbootjdbcdemo.dao;

public class RepositoryFactory {
    public static final IRepository getRepository(String tableName) {
        IRepository repository = null;
        switch (tableName) {
            case "BOOK":
                repository = new BookRepository();
                break;
            default:
                System.out.println("Not found repository for table " + tableName);
                break;
        }
        return repository;
    }
}
