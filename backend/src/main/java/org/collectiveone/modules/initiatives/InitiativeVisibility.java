package org.collectiveone.modules.initiatives;

public enum InitiativeVisibility {
	PRIVATE, // only members of this initiative
	PARENTS, // only members of this initiative or members of a its parents genealogy
	ECOSYSTEM, // only members of the initiative ecosystem. All parents and childs of the super initiative
	PUBLIC, // anyone
	INHERITED // as its parent
}
