$(document).ready(function(){

   function validateFormInput(){
         userInputEmail = $('[name="email"]').val();
         userInputPassword = $('[name=password]').val();
         
         if (!userInputEmail.match(/[A-Za-z0-9]+@[A-Za-z0-9]+\.[A-Za-z]{3}/)){
            $("#invalidEmail").css("display", "inline");
            $("#invalidPassword").css("display", "none");
            return false;
         }
         else if (!userInputPassword.match(/[A-Za-z0-9]{6,20}/)){
        	 $("#invalidEmail").css("display", "none");
        	 $("#invalidPassword").css("display", "inline");
        	 return false;
         }
         else {
        	 $("#invalidEmail").css("display", "none");
        	 $("#invalidPassword").css("display", "none");
        	 return true;
         }
      }

      $("#inputForm").on("change", function(){
         validateFormInput();
      });
      
      $("#submit").on("click", function(e){
          if (!validateFormInput()){
        	  e.preventDefault();
          }
       });
});
