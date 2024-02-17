package ca.mcgill.ecse321.util;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Helper {
    

    // Compare two lists element-wise
public static boolean compareListsElementWise(List<?> list1, List<?> list2) {
    if (list1.size() != list2.size()) {
        return false;
    }

    Iterator<?> iterator1 = list1.iterator();
    Iterator<?> iterator2 = list2.iterator();

    while (iterator1.hasNext() && iterator2.hasNext()) {
        Object element1 = iterator1.next();
        Object element2 = iterator2.next();

        if (!Objects.equals(element1, element2)) {
            return false;
        }
    }

    return true;
}
}
