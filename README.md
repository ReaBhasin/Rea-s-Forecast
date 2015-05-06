# Rea-s-Forecast

Outstanding Issues
* Currently, fragments are refreshed by detaching and reattaching them. Instead, we should probably use notifydatasetchanged when the AsyncTasks are completed to refresh the view. I have not yet been able to implement this successfully.
* Attempting to switch activities (i.e. trying to go to the settings activity to to modify the zip code) will cause the app to crash because the state of the fragment cannot be saved. My best guess at the moment is that this is related to the issue mentioned above.
* The 'Three Day Forecast' view was originally working. While making additional changes, that tab/fragment no longer displays programmatically created views, but only displays views defined in the xml layout. I have been debugging this issue for a while now and it looks like data is successfully returned from the api call (and will display successfully to any other fragment), but the programmatically created table rows are not visible.
