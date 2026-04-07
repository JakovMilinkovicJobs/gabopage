package proj.gabopage.controller.dto;

import java.util.List;

public record ReorderRequest(List<Long> orderedIds) {
}
