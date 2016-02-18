package recaf.backtrack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.cps.ED;
import recaf.core.cps.SD;

public class Backtrack<R> extends AbstractJavaImpl<R> {

	public List<R> Method(SD<R> body) {
		List<R> result = new ArrayList<>();
		try {
			body.accept(null, ret -> {
				result.add(ret);
			}, () -> {}, l -> {}, l -> {}, exc -> { throw new RuntimeException(exc); });
		}
		catch (Fail f) {
			
		}
		return result;
	}
	
	private static final class Fail extends RuntimeException {
		
	}
	
	public <T> SD<R> Choose(ED<Iterable<T>> choices, Function<? super T, SD<R>> body) {
		return (label, rho, sigma, contin, brk, err) -> {
			choices.accept(iter -> {
				for (T t: iter) {
					try {
						body.apply(t).accept(null, rho, sigma, contin, brk, err);
					}
					catch (Fail f) {
					}
				}
				throw new Fail();
			}, err);;
		};
	}
	
	public SD<R> Guard(ED<Boolean> cond) {
		return (label, rho, sigma, contin, brk, err) -> {
			cond.accept(b -> {
				if (!b) {
					throw new Fail();
				}
				sigma.call();
			}, err);
		};
	}
	
	@Override
	public SD<R> Return() {
		throw new AssertionError("Cannot not return a value when backtracking");
	}
	
	
}
