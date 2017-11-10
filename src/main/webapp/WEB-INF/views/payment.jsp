<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<html  lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Clearmydues</title>
</head>


<body class="bg-body" style="margin: 0px; background: #efefef; font-family: Arial, Helvetica, Sans-serif">


<div style="width: 100%; height: auto; overflow: auto; padding: 0 6%; background: white">

    <div class="logo12">
        <img src="https://s3-ap-southeast-1.amazonaws.com/email-images-cmd/images/cms-logo.png" style="width: 100%">
    </div>

</div>

<style>
    .logo12 {
    float: left;
    width: 200px;
    height: 48px;
    margin: 23px 0;
}
</style>



<!--strip-->
<div class="full-width bg-strip">
    <div class="container">
        <div class="top-strip">




            <!--heading-->
            <div class="strip-heading" style="background: #e35160; text-align: center;
    padding: 1px 0px; color: white; font-family: Arial, Helvetica, Sans-serif">
                <h3> Your Transaction Details </h3>
            </div>
            <!--/heading-->



        </div>
    </div>
</div>
<!--/strip-->

<div class="full-width bg-body">
    <div class="container">
        <div class="stepAll">

            <div class="rd-pay-block">
                <p>${model.errorMSG}</p>
                <table class="rd-pay-table">
                    <tr>
                        <td>CMD Transaction ID</td>
                        <td>${model.cmdTxnId}</td>
                    </tr>
                    <tr>
                        <td>Payment Transaction ID</td>
                        <td>${model.gateWayTxnId}</td>
                    </tr>
                    <tr>
                        <td>Amount</td>
                        <td>Rs.  ${model.amount}</td>
                    </tr>
                </table>

                <div class="rd-pay-acc-btn" >
                    <a href="https://clearmydues.com/#/accounts">Go to Accounts Page</a>
                </div>

            </div>





        </div>
    </div>
</div>

<style>
    .rd-pay-block{
        width: 600px; height: 300px; padding: 7px; margin-left: auto; margin-right: auto; margin-top: 0px; }
    .rd-pay-block > p{
        text-align: center; margin-top: 10px; color: #3F51B5; font-weight: bold; background: #d9f0ff; border-radius: 10px; padding: 10px 5px; }
    .rd-pay-table{width: 100%; border-collapse: collapse; background: white; margin-top: 20px; border-radius: 5px; font-size: 14px;}
    .rd-pay-table tr td{padding: 15px 10px; border-bottom: 1px solid #d5d5d5}
    .rd-pay-table tr:last-child td{padding: 15px 10px; border-bottom: 0px solid #d5d5d5}
    .rd-pay-acc-btn{text-align: center; margin-top: 20px}
    .rd-pay-acc-btn a{background: #06e; color: white; text-decoration: none; padding: 10px 20px; border-radius: 10px;}

    @media only screen and (max-width:767px) and (min-width:320px) {
        .rd-pay-block{width: 300px;}
        .logo12 {margin: 5px 0}
    }


</style>



</body>
</html>