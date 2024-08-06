package br.com.acmeairlines.helpers;

import java.util.HashMap;
import java.util.Map;

public class RoleMapper {
    private static final Map<String, Integer> roleToIdMap = new HashMap<>();

    static {
        roleToIdMap.put("ROLE_ADMIN", 1);
        roleToIdMap.put("ROLE_USER", 2);
        roleToIdMap.put("ROLE_BAGGAGE_MANAGER", 3);
        roleToIdMap.put("ROLE_SUPPORT", 4);
    }

    public static Integer getRoleId(String roleName) {
        return roleToIdMap.get(roleName);
    }
}