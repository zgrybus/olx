import { createFileRoute } from '@tanstack/react-router';
import { Card, CardContent } from '@/components/ui/card';
import { Field, FieldDescription, FieldLabel } from '@/components/ui/field';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Button } from '@/components/ui/button';

export const Route = createFileRoute('/offers/create/')({
  component: CreateOfferPage,
});

function CreateOfferPage() {
  return (
    <main className="min-h-screen py-6 px-4">
      <div className="max-w-[1200px] mx-auto">
        <h2 className="text-2xl font-bold mb-8">Add Offer</h2>
        <div className="flex flex-col gap-3">
          <Card>
            <CardContent>
              <h3 className="text-xl font-bold mb-6">
                The more details the better!
              </h3>
              <Field className="md:max-w-7/12">
                <FieldLabel>Title of the offer</FieldLabel>
                <Input placeholder="e.g. iPhone 11 under warranty" />
                <FieldDescription>
                  Please enter at least 16 characters
                </FieldDescription>
              </Field>
            </CardContent>
          </Card>
          <Card>
            <CardContent>
              <Field className="md:max-w-7/12">
                <FieldLabel>Description</FieldLabel>
                <Textarea
                  className="min-h-60"
                  placeholder="Enter the information that would be important to you when viewing such an advertisement."
                />
                <FieldDescription>
                  Please enter at least 40 characters
                </FieldDescription>
              </Field>
            </CardContent>
          </Card>
          <Card>
            <CardContent>
              <h3 className="text-xl font-bold mb-6">Details</h3>
              <Field className="md:max-w-7/12">
                <FieldLabel>Price</FieldLabel>
                <Input placeholder="2000" />
              </Field>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="flex md:justify-end">
              <Button size="lg" className="flex-1 md:flex-initial">
                Add Offer
              </Button>
            </CardContent>
          </Card>
        </div>
      </div>
    </main>
  );
}
