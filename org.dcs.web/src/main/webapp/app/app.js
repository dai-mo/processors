/**
 * Created by cmathew on 31/12/15.
 */
'use strict';

// Declare app level module which depends on views, and components
var dcs = angular.module('dcs', [
    'dcs.version'
])


dcs.directive('containerResize', function(){
	// Runs during compile
	return {
		// name: '',
		// priority: 1,
		// terminal: true,
		scope: {
			crType: '@'
		}, 
		// controller: function($scope, $element, $attrs, $transclude) {},
		// require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
		restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment
		//template: 'Test'
		// templateUrl: '',
		//replace: true,
		// transclude: true,
		// compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
		link: function(scope, iElm, iAttrs, controller) {
			
			var type = scope.crType;
			
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
				var offset = 0, prevFlexBasis = 1, nextFlexBasis = 1;
				switch(type) {
					case 'column':
						offset = start - event.clientY;
						var prevFlexBasis = prevElmHeight - offset;
						var nextFlexBasis = nextElmHeight + offset;
						break;
					case 'row':
						offset = start - event.clientX;
						var prevFlexBasis = prevElmWidth - offset;
						var nextFlexBasis = nextElmWidth + offset;
						break;

				}

				prevElm.style['flexBasis'] = prevFlexBasis + 'px';
				nextElm.style['flexBasis'] = nextFlexBasis + 'px';


			};

			var startDrag = function(event) {
				
				switch(type) {
					case 'column':
						start = event.clientY;
						break;
					case 'row':
						start = event.clientX;
						break;
					default:
						return;
				}
				
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

dcs.directive('wsView', function(){
	// Runs during compile
	return {
		// name: '',
		// priority: 1,
		// terminal: true,
		scope: {
			viewName: '@'
		}, // {} = isolate, true = child, false/undefined = no change
		// controller: function($scope, $element, $attrs, $transclude) {},
		// require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
		// restrict: 'A', // E = Element, A = Attribute, C = Class, M = Comment
		// template: '',
		 templateUrl: 'ws-view.htm'
		// replace: true,
		// transclude: true,
		// compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),

	};
});