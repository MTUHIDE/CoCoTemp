$(function () {

    // Show modal to submit form
    $('#news-post-form').on('submit', function(e){
        e.preventDefault();

        // Check if form has all required values
        if ( this.checkValidity() ) {
            // Check if final submit button was clicked to submit
            if (this.submitted) {
                this.submit();
            }
            //Open modal when initial submit button is clicked
            else {
                $("#confirm-submit-modal").modal();
            }
        }

    });
});