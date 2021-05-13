package com.todo.app.data.service;

import java.util.List;

public interface AdminService {

    List<Object> executeSql(String query);
}
