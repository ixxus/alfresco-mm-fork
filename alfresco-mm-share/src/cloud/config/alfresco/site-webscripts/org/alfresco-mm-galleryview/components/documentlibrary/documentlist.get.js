if (model.preferences.simpleView != null && model.preferences.simpleView === true || model.preferences.simpleView === false)
{
   model.preferences.viewRendererName = (model.preferences.simpleView ? "simple" : "detailed");
}
else
{
   if (model.preferences.simpleView == null && model.preferences.viewRendererName == null)
   {
      model.preferences.viewRendererName = "detailed";
   }
}
model.viewRendererNames.push("gallery");