
/* 
 * 
 * underbody.js
 * javascript DOM library
 * 
 * 
 * Robert Kosin
 * 
 */


underbody = function(argument)
{
	if(!(this instanceof underbody))
		return new underbody(argument);
	
	this.elements = Array.prototype.slice.call(document.querySelectorAll(argument));
	
	return this;
};
var _ = underbody;
