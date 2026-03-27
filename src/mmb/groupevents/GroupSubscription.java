package mmb.groupevents;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public record GroupSubscription<Tcriterion, Tevent>(
	Set<Tcriterion> permittedItems, Consumer<Tevent> action
) {
	public GroupSubscription{
		Objects.requireNonNull(permittedItems, "permittedItems is null");
		Objects.requireNonNull(action, "action is null");
	}
	
	public GroupSubscription filterPermittedItems(Predicate<? super Tcriterion> predicate) {
		Set<Tcriterion> filteredSet = permittedItems.stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
		return new GroupSubscription(filteredSet, action);
	}
}
