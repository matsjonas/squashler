/* Filters the table at the overview page */
function filterPlayers() {
    playerOne = jQuery('#playerOne option:selected').attr('value');
    playerTwo = jQuery('#playerTwo option:selected').attr('value');
    if (playerOne == '' && playerTwo == '') {
        // No selected, show all.
        jQuery('#matchTable tbody tr').each(function() {
            jQuery(this).show();
        });
    } else if (playerOne != '' && (playerTwo == '' || playerTwo == playerOne)) {
        // One player is selected.
        jQuery('#matchTable tbody tr').each(function() {
            left = jQuery(this).find('.playerLeft').html();
            right = jQuery(this).find('.playerRight').html();
            if (playerOne == left || playerOne == right) {
                jQuery(this).show();
            } else {
                jQuery(this).hide();
            }
        });
    } else {
        jQuery('#matchTable tbody tr').each(function() {
            left = jQuery(this).find('.playerLeft').html();
            right = jQuery(this).find('.playerRight').html();
            if ((playerOne == left && playerTwo == right) ||
                (playerOne == right && playerTwo == left)) {
                jQuery(this).show();
            } else {
                jQuery(this).hide();
            }
        });
    }
}

/* Called when playerOne is changed. */
function selectedPlayerOne() {
    playerOne = jQuery('#playerOne option:selected').attr('value');
    jQuery('#playerTwo option:disabled').removeAttr('disabled');
    if (playerOne == '') {
        // Hide and reset player two select box.
        jQuery('#playerTwo').hide();
        jQuery('#playerTwo').val('');
    } else {
        // Show and disable the player that is selected in #playerOne
        jQuery('#playerTwo').show();
        jQuery('#playerTwo option[value=' + playerOne + ']').attr('disabled','disabled');
    }
}