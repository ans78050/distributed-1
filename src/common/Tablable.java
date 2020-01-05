package common;

import common.model.Student;

import java.util.List;

public interface Tablable {
    String[] toTable();

    static <T extends Tablable> String[][] of(List<T> list, int width) {
        String[][] content = new String[list.size()][width];
        for (int i = 0; i < list.size(); i++) {
            content[i] = list.get(i).toTable();
        }
        return content;
    }
}
