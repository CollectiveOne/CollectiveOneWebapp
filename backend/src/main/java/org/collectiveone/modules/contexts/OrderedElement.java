package org.collectiveone.modules.contexts;

public interface OrderedElement {
	
	public OrderedElement getBeforeElement();

	public void setBeforeElement(OrderedElement beforeElement);

	public OrderedElement getAfterElement();

	public void setAfterElement(OrderedElement afterElement);
	
}
