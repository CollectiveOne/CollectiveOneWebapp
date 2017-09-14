package org.collectiveone.modules.initiatives;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Initiative.class)
public class InitiativeMeta_ {
	public static volatile SetAttribute<InitiativeMeta, InitiativeTag> tags;
}
