var holder = d3.select("body") // select the 'body' element
      .append("svg")           // append an SVG element to the body
      .attr("width", 1200)      
      .attr("height", 600);   
// draw a rectangle - pitch
holder.append("rect")        // attach a rectangle
    .attr("x", 0)         // position the left of the rectangle
    .attr("y", 0)          // position the top of the rectangle
    .attr("height", 500)    // set the height
    .attr("width", 1000)    // set the width
    .style("stroke-width", 5)    // set the stroke width
    .style("stroke", "#001400")    // set the line colour
    .style("fill", "#80B280");    // set the fill colour 
// draw a rectangle - halves
holder.append("rect")        // attach a rectangle
    .attr("x", 0)         // position the left of the rectangle
    .attr("y", 0)          // position the top of the rectangle
    .attr("height", 500)    // set the height
    .attr("width", 500)    // set the width
    .style("stroke-width", 5)    // set the stroke width
    .style("stroke", "#001400")    // set the line colour
    .style("fill", "#80B280");    // set the fill colour 
// draw a circle - center circle
holder.append("circle")          // attach a circle
    .attr("cx", 500)             // position the x-centre
    .attr("cy", 250)             // position the y-centre
    .attr("r", 50)               // set the radius
    .style("stroke-width", 5)    // set the stroke width
    .style("stroke", "#001400")      // set the line colour
    .style("fill", "none");      // set the fill colour
// draw a rectangle - penalty area 1
holder.append("rect")        // attach a rectangle
    .attr("x", 0)         // position the left of the rectangle
    .attr("y", 105)          // position the top of the rectangle
    .attr("height", 290)    // set the height
    .attr("width", 170)    // set the width
    .style("stroke-width", 5)    // set the stroke width
    .style("stroke", "#001400")    // set the line colour
    .style("fill", "#80B280");    // set the fill colour 
// draw a rectangle - penalty area 2
holder.append("rect")        // attach a rectangle
    .attr("x", 830)         // position the left of the rectangle
    .attr("y", 105)          // position the top of the rectangle
    .attr("height", 290)    // set the height
    .attr("width", 170)    // set the width
    .style("stroke-width", 5)    // set the stroke width
    .style("stroke", "#001400")    // set the line colour
    .style("fill", "#80B280");    // set the fill colour 
// draw a rectangle - six yard box 1
holder.append("rect")        // attach a rectangle
    .attr("x", 0)         // position the left of the rectangle
    .attr("y", 184)          // position the top of the rectangle
    .attr("height", 132)    // set the height
    .attr("width", 60)    // set the width
    .style("stroke-width", 5)    // set the stroke width
    .style("stroke", "#001400")    // set the line colour
    .style("fill", "#80B280");    // set the fill colour 
// draw a rectangle - six yard box 2
holder.append("rect")        // attach a rectangle
    .attr("x", 940)         // position the left of the rectangle
    .attr("y", 184)          // position the top of the rectangle
    .attr("height", 132)    // set the height
    .attr("width", 60)    // set the width
    .style("stroke-width", 5)    // set the stroke width
    .style("stroke", "#001400")    // set the line colour
    .style("fill", "#80B280");    // set the fill colour 
// draw a circle - penalty spot 1
holder.append("circle")        // attach a circle
    .attr("cx", 120)           // position the x-centre
    .attr("cy", 250)           // position the y-centre
    .attr("r", 5)             // set the radius
    .style("fill", "#001400");     // set the fill colour
// draw a circle - penalty spot 2
holder.append("circle")        // attach a circle
    .attr("cx", 880)           // position the x-centre
    .attr("cy", 250)           // position the y-centre
    .attr("r", 5)             // set the radius
    .style("fill", "#001400");     // set the fill colour
// draw a circle - center spot
holder.append("circle")        // attach a circle
    .attr("cx", 500)           // position the x-centre
    .attr("cy", 250)           // position the y-centre
    .attr("r", 5)             // set the radius
    .style("fill", "#001400");     // set the fill colour
// penalty box semi-circle 1
var vis = d3.select("body").append("svg")
var pi = Math.PI;
    
var arc = d3.svg.arc()
    .innerRadius(70)
    .outerRadius(75)
    .startAngle(0.75) //radians
    .endAngle(2.4) //just radians
    
var arc2 = d3.svg.arc()
    .innerRadius(70)
    .outerRadius(75)
    .startAngle(-0.75) //radians
    .endAngle(-2.4) //just radians
    holder.append("path")
    .attr("d", arc)
    .attr("fill", "#001400")
    .attr("transform", "translate(120,250)");
    holder.append("path")
    .attr("d", arc2)
    .attr("fill", "#001400")
    .attr("transform", "translate(880,250)");

