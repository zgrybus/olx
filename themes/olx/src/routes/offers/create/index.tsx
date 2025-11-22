import { createFileRoute } from '@tanstack/react-router';
import { useForm } from '@tanstack/react-form';
import z from 'zod';
import { Card, CardContent } from '@/components/ui/card';
import {
  Field,
  FieldDescription,
  FieldError,
  FieldLabel,
} from '@/components/ui/field';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Button } from '@/components/ui/button';
import { $olxApi } from '@/apis/olx';

export const Route = createFileRoute('/offers/create/')({
  component: CreateOfferPage,
});

const createOfferSchema = z.object({
  title: z
    .string()
    .min(16, 'The title is too short. Please add more details.')
    .max(70, `The title is too long. Don't add so much detail.`),
  description: z
    .string()
    .min(40, 'The description is too short. Please add more details.')
    .max(9000, `The description is too long. Don't add so much detail.`),
  price: z.coerce
    .number()
    .min(0, 'Incorrect price, the price should be in the format: 1234567.50'),
});

function CreateOfferPage() {
  const { mutate: createOfferMutate } = $olxApi.useMutation(
    'post',
    '/api/offers',
  );

  const form = useForm({
    defaultValues: {
      title: '',
      description: '',
      price: '',
    },
    validators: {
      onChange: createOfferSchema,
    },
    onSubmit: (data) => {
      createOfferMutate({ body: data.value });
    },
  });

  return (
    <main className="min-h-screen py-6 px-4">
      <div className="max-w-[1200px] mx-auto">
        <h2 className="text-2xl font-bold">Add Offer</h2>
        <form
          className="flex flex-col gap-3 mt-8"
          onSubmit={(e) => {
            e.preventDefault();
            e.stopPropagation();
            form.handleSubmit();
          }}
        >
          <Card>
            <CardContent>
              <h3 className="text-xl font-bold mb-6">
                The more details the better!
              </h3>
              <form.Field
                name="title"
                children={(field) => (
                  <Field
                    className="md:max-w-7/12"
                    data-invalid={field.state.meta.errors.length > 0}
                  >
                    <FieldLabel htmlFor={field.name}>
                      Title of the offer
                    </FieldLabel>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      placeholder="e.g. iPhone 11 under warranty"
                      aria-invalid={field.state.meta.errors.length > 0}
                    />
                    <FieldError errors={field.state.meta.errors} />
                    <FieldDescription>
                      <div className="flex justify-between gap-2">
                        <span>Please enter at least 16 characters</span>
                        <span>{field.state.value.length}/70</span>
                      </div>
                    </FieldDescription>
                  </Field>
                )}
              />
            </CardContent>
          </Card>
          <Card>
            <CardContent>
              <form.Field
                name="description"
                children={(field) => (
                  <Field
                    className="md:max-w-7/12"
                    data-invalid={field.state.meta.errors.length > 0}
                  >
                    <FieldLabel htmlFor={field.name}>Description</FieldLabel>
                    <Textarea
                      className="min-h-60"
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      placeholder="Enter the information that would be important to you when viewing such an advertisement."
                      aria-invalid={field.state.meta.errors.length > 0}
                    />
                    <FieldError errors={field.state.meta.errors} />
                    <FieldDescription>
                      <div className="flex justify-between gap-2">
                        <span>Please enter at least 40 characters</span>
                        <span>{field.state.value.length}/9000</span>
                      </div>
                    </FieldDescription>
                  </Field>
                )}
              />
            </CardContent>
          </Card>
          <Card>
            <CardContent>
              <h3 className="text-xl font-bold mb-6">Details</h3>
              <form.Field
                name="price"
                children={(field) => (
                  <Field
                    className="md:max-w-7/12"
                    data-invalid={field.state.meta.errors.length > 0}
                  >
                    <FieldLabel htmlFor={field.name}>Price</FieldLabel>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="number"
                      placeholder="2000"
                      aria-invalid={field.state.meta.errors.length > 0}
                    />
                    <FieldError errors={field.state.meta.errors} />
                  </Field>
                )}
              />
            </CardContent>
          </Card>
          <Card>
            <CardContent className="flex md:justify-end">
              <form.Subscribe
                selector={(state) => [state.isSubmitting]}
                children={([isSubmitting]) => (
                  <Button
                    type="submit"
                    size="lg"
                    className="flex-1 md:flex-initial"
                    loading={isSubmitting}
                  >
                    Add Offer
                  </Button>
                )}
              />
            </CardContent>
          </Card>
        </form>
      </div>
    </main>
  );
}
