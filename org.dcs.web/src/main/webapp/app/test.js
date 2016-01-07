dcs.directive('containerResize', function(){
	// Runs during compile
	return {
		// name: '',
		// priority: 1,
		// terminal: true,
		// scope: {}, // {} = isolate, true = child, false/undefined = no change
		// controller: function($scope, $element, $attrs, $transclude) {},
		// require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
		restrict: 'A', // E = Element, A = Attribute, C = Class, M = Comment
		//template: 'Test'
		// templateUrl: '',
		//replace: true,
		// transclude: true,
		// compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
		link: function($scope, iElm, iAttrs, controller) {
			
			var start, minimum = 20;

			var prevElm,prevElmStyle,prevElmWidth,prevElmHeight;
			var nextElm,nextElmStyle,nextElmWidth,nextElmHeight;

			var updateProperties = function() {
				var siblings = iElm.parent().children();
				if(siblings.length != 3) {
					alert('Flex container contains incorrect number of children');
				}
				prevElm = siblings[0];		
				
				prevElmStyle = window.getComputedStyle(prevElm, null);
				prevElmWidth = parseInt(prevElmStyle.getPropertyValue('width'));
				prevElmHeight = parseInt(prevElmStyle.getPropertyValue('height'));

				nextElm = siblings[2];	
				
				nextElmStyle = window.getComputedStyle(nextElm, null);
				nextElmWidth = parseInt(nextElmStyle.getPropertyValue('width'));
				nextElmHeight = parseInt(nextElmStyle.getPropertyValue('height'));
			};

			updateProperties();
                    
			var endDrag = function(event) {
				document.removeEventListener('mouseup', endDrag, false);
				document.removeEventListener('mousemove', drag, false);
			};

			var drag = function(event) {
				var offset = start - event.clientY;
				var prevElmNewHeight = prevElmHeight - offset;
				var nextElmNewHeight = nextElmHeight + offset;
				if(prevElmNewHeight > minimum && nextElmNewHeight > minimum) {
					prevElm.style['flexBasis'] = prevElmNewHeight + 'px';
					nextElm.style['flexBasis'] = nextElmNewHeight + 'px';
				}
			};

			var startDrag = function(event) {	
				start = event.clientY;
				updateProperties();
				document.addEventListener('mouseup', endDrag, false);
				document.addEventListener('mousemove', drag, false);
			};

			
			iElm.on('mousedown', function(e) {				
				if(e.which === 1) {
					startDrag(e);
				}
			});
		}
	};
});