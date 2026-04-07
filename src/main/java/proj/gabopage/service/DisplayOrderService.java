package proj.gabopage.service;

import org.springframework.stereotype.Service;
import proj.gabopage.model.DisplayOrderEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DisplayOrderService {

    public <T extends DisplayOrderEntity> int nextDisplayOrder(List<T> items) {
        return items.stream()
                .map(DisplayOrderEntity::getDisplayOrder)
                .filter(order -> order != null)
                .max(Integer::compareTo)
                .orElse(-1) + 1;
    }

    public <T extends DisplayOrderEntity> void applyOrder(List<T> items, List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.size() != items.size()) {
            throw new IllegalArgumentException("Invalid item order.");
        }

        Map<Long, T> itemsById = new HashMap<>();
        for (T item : items) {
            itemsById.put(item.getId(), item);
        }

        for (int index = 0; index < orderedIds.size(); index++) {
            Long id = orderedIds.get(index);
            T item = itemsById.get(id);
            if (item == null) {
                throw new IllegalArgumentException("Invalid item in order payload.");
            }
            item.setDisplayOrder(index);
        }
    }

    public <T extends DisplayOrderEntity> void normalize(List<T> items) {
        for (int index = 0; index < items.size(); index++) {
            items.get(index).setDisplayOrder(index);
        }
    }
}
