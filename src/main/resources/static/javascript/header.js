$(document).ready(function () {
    $("#login").click(
        (e) => {
            e.preventDefault();
            $("#loginElement").toggleClass("hidden");
            return false;
        }
    )
    $("#logout").click(
        (e) => {
            e.preventDefault();
            $("#logoutElement").submit();
            return false;
        }
    );
})

$(document).ready(function () {
    $("#menuButton").click(
        (e) => {
            e.preventDefault();
            $("#menuNavigationElement").toggleClass("hidden");
            return false;
        }
    )
})
