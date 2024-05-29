package org.example.springbootjdbcdemo.dao;

public class DaoFactory {
    public static final GenericDao getRepository(String tableName) {
        GenericDao repository = null;
        switch (tableName) {
            case "BOOK":
                repository = new BookDao();
                break;
            default:
                System.out.println("Not found repository for table " + tableName);
                break;
        }
        return repository;
    }
}
