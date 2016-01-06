/**
* Created by cmathew on 04/01/16.
*/
dcs.directive('mobilise', function(){
  return {
    restrict : 'E',
    templateUrl: function(elem, attr) {
      return attr.type + '-view.htm';
    }
  }
});
