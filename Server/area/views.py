from typing import final
from django.http.response import HttpResponse, JsonResponse
from rest_framework import viewsets
from .serializer import AreaSerializer
from .models import Area

class AreaViewSet(viewsets.ModelViewSet):
    queryset = Area.objects.all()
    serializer_class = AreaSerializer

def get_area_by_senses(request):
    print(type(request))
    if request.method == 'POST':
        sight = request.POST.get('sight')
        touch = request.POST.get('touch')
        taste = request.POST.get('taste')

        print(sight)
        print(touch)
        print(taste)

        sightResult = Area.objects.filter(sight = sight)
        touchResult = Area.objects.filter(touch = touch)
        tasteResult = Area.objects.filter(taste = taste)

        sightTouch = sightResult.filter(touch = touch)
        touchTaste = touchResult.filter(taste = taste)
        tasteSight = tasteResult.filter(sight = sight)

        finalResult = sightTouch.filter(taste = taste)
        print(finalResult)
        
        return HttpResponse(finalResult, content_type="application/json")