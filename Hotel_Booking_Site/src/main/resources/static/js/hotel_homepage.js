$(document).ready(function() {
	
	   function validateFormInput(){
         cityInput = $('[name="city"]').val();
         checkInInput = $('[name="checkInDate"]').val();
         checkOutInput = $('[name="checkOutDate"]').val();
         
         if (!cityInput.match(/[A-Za-z ]+/)){
            $("#invalidInput").css("display", "inline");
            return false;
         }
         
         else if (!checkInInput.match(/[0-9]{1,2}\/[0-9]{1,2}\/[0-9]{4}/)){
             $("#invalidInput").css("display", "inline");
             return false;
          }
         
         else if (!checkOutInput.match(/[0-9]{1,2}\/[0-9]{1,2}\/[0-9]{4}/)){
             $("#invalidInput").css("display", "inline");
             return false;
          }
          
         else {
        	 $("#invalidInput").css("display", "none");
          return true;
         }
      }
   
      $("#submit").on("click", function(e){
          if (!validateFormInput()){
           e.preventDefault();
          }
       });
       
       	$('#datepicker').datepicker({
		orientation: "top auto"
	});

});