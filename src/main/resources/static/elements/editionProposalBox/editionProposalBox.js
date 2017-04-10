function EditionProposalBox(container_id,proposalData) {
	// Parent constructor
	this.container = $(container_id);
	this.proposal = proposalData;
	if(proposalData) {
		this.draw();
	}
};

//Inheritance
EditionProposalBox.prototype.draw = function() {
	this.container.load("/elements/editionProposalBox/editionProposalBox.html",this.EditionProposalBox.bind(this));
}

EditionProposalBox.prototype.EditionProposalBox = function() {
	
	$("#prop-content",this.container).html(markdown.toHTML(this.proposal.edition));
	$("#prop-author",this.container).html(
			"proposed by " + getUserPageLink(this.proposal.proposerUsername) + " " + 
			getTimeStrSince(this.proposal.creationDate) + " ago <a target='_blank' href='/v/decision/"+this.proposal.acceptDec.id+"'>(go to decision)</a>");
	$("#prop-decision",this.container).html("")
	
}